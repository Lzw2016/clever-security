package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
import org.clever.common.utils.tuples.TupleFour;
import org.clever.common.utils.tuples.TupleThree;
import org.clever.security.client.UpdatePasswordSupportClient;
import org.clever.security.crypto.PasswordEncoder;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.User;
import org.clever.security.entity.ValidateCode;
import org.clever.security.mapper.JwtTokenMapper;
import org.clever.security.mapper.UserMapper;
import org.clever.security.mapper.UserSecurityContextMapper;
import org.clever.security.mapper.ValidateCodeMapper;
import org.clever.security.utils.ConvertUtils;
import org.clever.security.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:17 <br/>
 */
@Transactional
@Primary
@Service
@Slf4j
public class UpdatePasswordSupportService implements UpdatePasswordSupportClient {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ValidateCodeMapper validateCodeMapper;
    @Autowired
    private JwtTokenMapper jwtTokenMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserSecurityContextMapper userSecurityContextMapper;
    @Autowired
    private VerifyValidateCodeService verifyValidateCodeService;
    @Autowired
    private SendValidateCodeService sendValidateCodeService;

    @Override
    public GetUpdatePasswordCaptchaRes getUpdatePasswordCaptcha(GetUpdatePasswordCaptchaReq req) {
        TupleFour<String, String, String, Date> tuple = getCaptcha(req.getDomainId(), req.getEffectiveTimeMilli());
        GetUpdatePasswordCaptchaRes res = new GetUpdatePasswordCaptchaRes();
        res.setCode(tuple.getValue1());
        res.setDigest(tuple.getValue2());
        res.setCodeContent(tuple.getValue3());
        res.setExpiredTime(tuple.getValue4());
        return res;
    }

    @Override
    public VerifyUpdatePasswordCaptchaRes verifyUpdatePasswordCaptcha(VerifyUpdatePasswordCaptchaReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCodeService.verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_13,
                EnumConstant.ValidateCode_SendChannel_0,
                req.getCaptcha(),
                req.getCaptchaDigest(),
                null);
        VerifyUpdatePasswordCaptchaRes res = new VerifyUpdatePasswordCaptchaRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public SendSmsUpdatePasswordValidateCodeRes sendSmsUpdatePasswordValidateCode(SendSmsUpdatePasswordValidateCodeReq req) {
        final Date now = new Date();
        final Date dayStart = DateTimeUtils.getDayStartTime(now);
        final Date dayEnd = DateTimeUtils.getDayEndTime(now);
        User user = userMapper.getByTelephone(req.getTelephone());
        if (user == null) {
            throw new BusinessException("手机未注册");
        }
        // 上一次短信验证码有效就直接返回
        ValidateCode lastEffective = validateCodeMapper.getLastEffectiveNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_14,
                EnumConstant.ValidateCode_SendChannel_1,
                req.getTelephone()
        );
        if (lastEffective != null) {
            // 返回
            return ConvertUtils.convertToSendSmsUpdatePasswordValidateCodeRes(lastEffective);
        }
        int sendCount = validateCodeMapper.getSendCount(
                req.getDomainId(),
                user.getUid(),
                EnumConstant.ValidateCode_Type_14,
                EnumConstant.ValidateCode_SendChannel_1,
                dayStart,
                dayEnd);
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("短信验证码发送次数超限");
        }
        // 发送短信验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newSmsValidateCode(now, req.getDomainId(), user.getUid(), req.getTelephone(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendEmail(EnumConstant.ValidateCode_Type_14, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_14);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_1);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendSmsUpdatePasswordValidateCodeRes(validateCode);
    }

    @Override
    public VerifySmsUpdatePasswordValidateCodeRes verifySmsUpdatePasswordValidateCode(VerifySmsUpdatePasswordValidateCodeReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCodeService.verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_14,
                EnumConstant.ValidateCode_SendChannel_1,
                req.getCode(),
                req.getCodeDigest(),
                req.getTelephone());
        VerifySmsUpdatePasswordValidateCodeRes res = new VerifySmsUpdatePasswordValidateCodeRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public SendEmailUpdatePasswordValidateCodeRes sendEmailUpdatePasswordValidateCode(SendEmailUpdatePasswordValidateCodeReq req) {
        final Date now = new Date();
        final Date dayStart = DateTimeUtils.getDayStartTime(now);
        final Date dayEnd = DateTimeUtils.getDayEndTime(now);
        User user = userMapper.getByEmail(req.getEmail());
        if (user == null) {
            throw new BusinessException("邮箱未注册");
        }
        // 上一次邮箱验证码有效就直接返回
        ValidateCode lastEffective = validateCodeMapper.getLastEffectiveNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_15,
                EnumConstant.ValidateCode_SendChannel_2,
                req.getEmail()
        );
        if (lastEffective != null) {
            // 返回
            return ConvertUtils.convertToSendEmailUpdatePasswordValidateCodeRes(lastEffective);
        }
        int sendCount = validateCodeMapper.getSendCount(
                req.getDomainId(),
                user.getUid(),
                EnumConstant.ValidateCode_Type_15,
                EnumConstant.ValidateCode_SendChannel_2,
                dayStart,
                dayEnd);
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("邮箱验证码发送次数超限");
        }
        // 发送邮箱验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newSmsValidateCode(now, req.getDomainId(), user.getUid(), req.getEmail(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendEmail(EnumConstant.ValidateCode_Type_15, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_15);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_2);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendEmailUpdatePasswordValidateCodeRes(validateCode);
    }

    @Override
    public VerifyEmailUpdatePasswordValidateCodeRes verifyEmailUpdatePasswordValidateCode(VerifyEmailUpdatePasswordValidateCodeReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCodeService.verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_15,
                EnumConstant.ValidateCode_SendChannel_2,
                req.getCode(),
                req.getCodeDigest(),
                req.getEmail());
        VerifyEmailUpdatePasswordValidateCodeRes res = new VerifyEmailUpdatePasswordValidateCodeRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public InitPasswordRes initPassword(InitPasswordReq req) {
        User user = userMapper.getByUid(req.getUid());
        if (user == null || StringUtils.isNotBlank(user.getPassword())) {
            throw new BusinessException("已设置密码,无法再次设置");
        }
        User update = new User();
        update.setUid(user.getUid());
        update.setPassword(passwordEncoder.encode(req.getInitPassword()));
        updatePassword(update);
        InitPasswordRes res = new InitPasswordRes();
        res.setSuccess(true);
        return res;
    }

    @Override
    public UpdatePasswordRes updatePassword(UpdatePasswordReq req) {
        User user = userMapper.getByUid(req.getUid());
        if (user == null || !passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new BusinessException("密码不正确");
        }
        User update = new User();
        update.setUid(user.getUid());
        update.setPassword(passwordEncoder.encode(req.getNewPassword()));
        updatePassword(update);
        UpdatePasswordRes res = new UpdatePasswordRes();
        res.setSuccess(true);
        return res;
    }

    /**
     * 生成图片验证码
     *
     * @return {@code TupleFour<Code, Digest, image, ExpiredTime>}
     */
    protected TupleFour<String, String, String, Date> getCaptcha(Long domainId, Integer effectiveTimeMilli) {
        final Date now = new Date();
        ValidateCode validateCode = ValidateCodeUtils.newCaptchaValidateCode(now, domainId, effectiveTimeMilli);
        validateCode.setType(EnumConstant.ValidateCode_Type_13);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_0);
        byte[] image = ImageValidateCageUtils.createImage(validateCode.getCode());
        validateCodeMapper.insert(validateCode);
        return TupleFour.creat(validateCode.getCode(), validateCode.getDigest(), EncodeDecodeUtils.encodeBase64(image), validateCode.getExpiredTime());
    }

    protected void updatePassword(User update) {
        userMapper.updateById(update);
        // 禁用当前用户的Token
        final String disableReason = "用户更新密码";
        jwtTokenMapper.disableJwtTokenByUid(update.getUid(), disableReason);
        // 清除 user_security_context
        userSecurityContextMapper.deleteByUid(update.getUid());
    }
}
