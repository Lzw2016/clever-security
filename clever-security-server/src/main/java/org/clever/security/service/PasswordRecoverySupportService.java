package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
import org.clever.common.utils.tuples.TupleFour;
import org.clever.common.utils.tuples.TupleThree;
import org.clever.security.client.PasswordRecoverySupportClient;
import org.clever.security.crypto.PasswordEncoder;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.JwtToken;
import org.clever.security.entity.User;
import org.clever.security.entity.ValidateCode;
import org.clever.security.mapper.JwtTokenMapper;
import org.clever.security.mapper.UserDomainMapper;
import org.clever.security.mapper.UserMapper;
import org.clever.security.mapper.ValidateCodeMapper;
import org.clever.security.utils.ConvertUtils;
import org.clever.security.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/12 21:27 <br/>
 */
@Transactional
@Primary
@Service
@Slf4j
public class PasswordRecoverySupportService implements PasswordRecoverySupportClient {
    @Autowired
    private ValidateCodeMapper validateCodeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDomainMapper userDomainMapper;
    @Autowired
    private JwtTokenMapper jwtTokenMapper;
    @Autowired
    private SendValidateCodeService sendValidateCodeService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public GetSmsRecoveryCaptchaRes getSmsRecoveryCaptcha(GetSmsRecoveryCaptchaReq req) {
        TupleFour<String, String, String, Date> tuple = getPasswordRecoveryCaptcha(req.getDomainId(), req.getEffectiveTimeMilli());
        GetSmsRecoveryCaptchaRes res = new GetSmsRecoveryCaptchaRes();
        res.setCode(tuple.getValue1());
        res.setDigest(tuple.getValue2());
        res.setCodeContent(tuple.getValue3());
        res.setExpiredTime(tuple.getValue4());
        return res;
    }

    @Override
    public VerifySmsRecoveryCaptchaRes verifySmsRecoveryCaptcha(VerifySmsRecoveryCaptchaReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCode(req.getDomainId(), req.getCaptcha(), req.getCaptchaDigest());
        VerifySmsRecoveryCaptchaRes res = new VerifySmsRecoveryCaptchaRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public SendSmsRecoveryValidateCodeRes sendSmsRecoveryValidateCode(SendSmsRecoveryValidateCodeReq req) {
        final Date now = new Date();
        final Date dayStart = DateTimeUtils.getDayStartTime(now);
        final Date dayEnd = DateTimeUtils.getDayEndTime(now);
        User user = userMapper.getByTelephone(req.getTelephone());
        if (isInvalidUser(req.getDomainId(), now, user)) {
            throw new BusinessException("手机号未注册或是无效的账号");
        }
        // 上一次短信验证码有效就直接返回
        ValidateCode lastEffective = validateCodeMapper.getLastEffectiveNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_2,
                EnumConstant.ValidateCode_SendChannel_1,
                req.getTelephone()
        );
        if (lastEffective != null) {
            // 返回
            return ConvertUtils.convertToSendSmsRecoveryValidateCodeRes(lastEffective);
        }
        int sendCount = validateCodeMapper.getSendCount(
                req.getDomainId(),
                user.getUid(),
                EnumConstant.ValidateCode_Type_2,
                EnumConstant.ValidateCode_SendChannel_1,
                dayStart,
                dayEnd
        );
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("短信验证码发送次数超限");
        }
        // 发送短信验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newSmsValidateCode(now, req.getDomainId(), user.getUid(), req.getTelephone(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendSms(EnumConstant.ValidateCode_Type_2, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_2);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_1);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendSmsRecoveryValidateCodeRes(validateCode);
    }

    @Override
    public VerifySmsRecoveryValidateCodeRes verifySmsRecoveryValidateCode(VerifySmsRecoveryValidateCodeReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_SendChannel_1,
                req.getCode(),
                req.getCodeDigest(),
                req.getTelephone()
        );
        User user = userMapper.getByTelephone(req.getTelephone());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        VerifySmsRecoveryValidateCodeRes res = new VerifySmsRecoveryValidateCodeRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        res.setUid(user.getUid());
        return res;
    }

    @Override
    public GetEmailRecoveryCaptchaRes getEmailRecoveryCaptcha(GetEmailRecoveryCaptchaReq req) {
        TupleFour<String, String, String, Date> tuple = getPasswordRecoveryCaptcha(req.getDomainId(), req.getEffectiveTimeMilli());
        GetEmailRecoveryCaptchaRes res = new GetEmailRecoveryCaptchaRes();
        res.setCode(tuple.getValue1());
        res.setDigest(tuple.getValue2());
        res.setCodeContent(tuple.getValue3());
        res.setExpiredTime(tuple.getValue4());
        return res;
    }

    @Override
    public VerifyEmailRecoveryCaptchaRes verifyEmailRecoveryCaptcha(VerifyEmailRecoveryCaptchaReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCode(req.getDomainId(), req.getCaptcha(), req.getCaptchaDigest());
        VerifyEmailRecoveryCaptchaRes res = new VerifyEmailRecoveryCaptchaRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public SendEmailRecoveryValidateCodeRes sendEmailRecoveryValidateCode(SendEmailRecoveryValidateCodeReq req) {
        final Date now = new Date();
        final Date dayStart = DateTimeUtils.getDayStartTime(now);
        final Date dayEnd = DateTimeUtils.getDayEndTime(now);
        User user = userMapper.getByEmail(req.getEmail());
        if (isInvalidUser(req.getDomainId(), now, user)) {
            throw new BusinessException("邮箱未注册或是无效的账号");
        }
        // 上一次邮箱验证码有效就直接返回
        ValidateCode lastEffective = validateCodeMapper.getLastEffectiveNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_2,
                EnumConstant.ValidateCode_SendChannel_2,
                req.getEmail()
        );
        if (lastEffective != null) {
            // 返回
            return ConvertUtils.convertToSendEmailRecoveryValidateCodeRes(lastEffective);
        }
        int sendCount = validateCodeMapper.getSendCount(
                req.getDomainId(),
                user.getUid(),
                EnumConstant.ValidateCode_Type_2,
                EnumConstant.ValidateCode_SendChannel_2,
                dayStart,
                dayEnd
        );
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("邮件验证码发送次数超限");
        }
        // 发送邮件验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newEmailValidateCode(now, req.getDomainId(), user.getUid(), req.getEmail(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendEmail(EnumConstant.ValidateCode_Type_2, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_2);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_2);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendEmailRecoveryValidateCodeRes(validateCode);
    }

    @Override
    public VerifyEmailRecoveryValidateCodeRes verifyEmailRecoveryValidateCode(VerifyEmailRecoveryValidateCodeReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_SendChannel_2,
                req.getCode(),
                req.getCodeDigest(),
                req.getEmail()
        );
        User user = userMapper.getByEmail(req.getEmail());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        VerifyEmailRecoveryValidateCodeRes res = new VerifyEmailRecoveryValidateCodeRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        res.setUid(user.getUid());
        return res;
    }

    @Override
    public ResetPasswordReqRes resetPassword(ResetPasswordReq req) {
        final Date now = new Date();
        User user = userMapper.getByUid(req.getUid());
        if (isInvalidUser(req.getDomainId(), now, user)) {
            throw new BusinessException("账号不存在或是无效的账号");
        }
        User update = new User();
        update.setUid(user.getUid());
        update.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userMapper.updateById(update);
        // 禁用当前用户的Token
        List<JwtToken> jwtTokenList = jwtTokenMapper.getEffectiveTokenByUid(update.getUid());
        final String disableReason = "用户修改密码";
        for (JwtToken jwtToken : jwtTokenList) {
            jwtTokenMapper.disableJwtToken(jwtToken.getDomainId(), jwtToken.getId(), disableReason);
        }
        // 返回
        ResetPasswordReqRes res = new ResetPasswordReqRes();
        res.setSuccess(true);
        return res;
    }

    /**
     * @return {@code TupleFour<Code, Digest, image, ExpiredTime>}
     */
    protected TupleFour<String, String, String, Date> getPasswordRecoveryCaptcha(Long domainId, Integer effectiveTimeMilli) {
        final Date now = new Date();
        ValidateCode validateCode = ValidateCodeUtils.newCaptchaValidateCode(now, domainId, effectiveTimeMilli);
        validateCode.setType(EnumConstant.ValidateCode_Type_2);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_0);
        byte[] image = ImageValidateCageUtils.createImage(validateCode.getCode());
        validateCodeMapper.insert(validateCode);
        return TupleFour.creat(validateCode.getCode(), validateCode.getDigest(), EncodeDecodeUtils.encodeBase64(image), validateCode.getExpiredTime());
    }

    /**
     * 是否是无效的用户
     */
    protected boolean isInvalidUser(Long domainId, Date now, User user) {
        if (user == null) {
            return true;
        }
        if (!Objects.equals(user.getEnabled(), EnumConstant.User_Enabled_1) || (user.getExpiredTime() != null && now.compareTo(user.getExpiredTime()) >= 0)) {
            return true;
        }
        int exists = userDomainMapper.exists(domainId, user.getUid());
        return exists <= 0;
    }

    protected TupleThree<Boolean, String, Boolean> verifyValidateCode(long domainId, String captcha, String captchaDigest) {
        return verifyValidateCode(domainId, EnumConstant.ValidateCode_SendChannel_0, captcha, captchaDigest, null);
    }

    protected TupleThree<Boolean, String, Boolean> verifyValidateCode(long domainId, int sendChannel, String captcha, String captchaDigest, String sendTarget) {
        final Date now = new Date();
        ValidateCode validateCode = validateCodeMapper.getByDigest(domainId, captchaDigest);
        TupleThree<Boolean, String, Boolean> tuple;
        if (Objects.equals(EnumConstant.ValidateCode_SendChannel_0, sendChannel)) {
            tuple = ValidateCodeUtils.verifyValidateCode(validateCode, now, captcha, EnumConstant.ValidateCode_Type_2);
        } else {
            tuple = ValidateCodeUtils.verifyValidateCode(validateCode, now, captcha, EnumConstant.ValidateCode_Type_2, sendChannel, sendTarget);
        }
        // 更新验证码
        if (validateCode != null && validateCode.getValidateTime() == null) {
            ValidateCode update = new ValidateCode();
            update.setId(validateCode.getId());
            update.setValidateTime(now);
            validateCodeMapper.updateById(update);
        }
        return tuple;
    }
}
