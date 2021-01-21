package org.clever.security.service;

import org.clever.common.exception.BusinessException;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
import org.clever.common.utils.tuples.TupleFour;
import org.clever.common.utils.tuples.TupleThree;
import org.clever.security.client.BindSupportClient;
import org.clever.security.crypto.PasswordEncoder;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.User;
import org.clever.security.entity.ValidateCode;
import org.clever.security.mapper.UserDomainMapper;
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
 * 创建时间：2021/01/17 20:46 <br/>
 */
@Transactional
@Primary
@Service
public class BindSupportService implements BindSupportClient {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ValidateCodeMapper validateCodeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDomainMapper userDomainMapper;
    @Autowired
    private UserSecurityContextMapper userSecurityContextMapper;
    @Autowired
    private VerifyValidateCodeService verifyValidateCodeService;
    @Autowired
    private SendValidateCodeService sendValidateCodeService;

    @Override
    public GetBindEmailCaptchaRes getBindEmailCaptcha(GetBindEmailCaptchaReq req) {
        TupleFour<String, String, String, Date> tuple = getBindCaptcha(req.getDomainId(), req.getEffectiveTimeMilli(), EnumConstant.ValidateCode_Type_11);
        GetBindEmailCaptchaRes res = new GetBindEmailCaptchaRes();
        res.setCode(tuple.getValue1());
        res.setDigest(tuple.getValue2());
        res.setCodeContent(tuple.getValue3());
        res.setExpiredTime(tuple.getValue4());
        return res;
    }

    @Override
    public GetBindSmsCaptchaRes getBindSmsCaptcha(GetBindSmsCaptchaReq req) {
        TupleFour<String, String, String, Date> tuple = getBindCaptcha(req.getDomainId(), req.getEffectiveTimeMilli(), EnumConstant.ValidateCode_Type_9);
        GetBindSmsCaptchaRes res = new GetBindSmsCaptchaRes();
        res.setCode(tuple.getValue1());
        res.setDigest(tuple.getValue2());
        res.setCodeContent(tuple.getValue3());
        res.setExpiredTime(tuple.getValue4());
        return res;
    }

    @Override
    public VerifyBindEmailCaptchaRes verifyBindEmailCaptcha(VerifyBindEmailCaptchaReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCodeService.verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_11,
                EnumConstant.ValidateCode_SendChannel_0,
                req.getCaptcha(),
                req.getCaptchaDigest(),
                null);
        VerifyBindEmailCaptchaRes res = new VerifyBindEmailCaptchaRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public VerifyBindSmsCaptchaRes verifyBindSmsCaptcha(VerifyBindSmsCaptchaReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCodeService.verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_9,
                EnumConstant.ValidateCode_SendChannel_0,
                req.getCaptcha(),
                req.getCaptchaDigest(),
                null);
        VerifyBindSmsCaptchaRes res = new VerifyBindSmsCaptchaRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public SendBindEmailValidateCodeRes sendBindEmailValidateCode(SendBindEmailValidateCodeReq req) {
        final Date now = new Date();
        final Date dayStart = DateTimeUtils.getDayStartTime(now);
        final Date dayEnd = DateTimeUtils.getDayEndTime(now);
        User user = userMapper.getByEmail(req.getEmail());
        if (user != null) {
            int exists = userDomainMapper.exists(req.getDomainId(), user.getUid());
            if (exists > 0) {
                throw new BusinessException("邮箱已经注册");
            }
        }
        // 上一次邮箱验证码有效就直接返回
        ValidateCode lastEffective = validateCodeMapper.getLastEffectiveNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_12,
                EnumConstant.ValidateCode_SendChannel_2,
                req.getEmail()
        );
        if (lastEffective != null) {
            // 返回
            return ConvertUtils.convertToSendBindEmailValidateCodeRes(lastEffective);
        }
        int sendCount = validateCodeMapper.getSendCountNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_12,
                EnumConstant.ValidateCode_SendChannel_2,
                req.getEmail(),
                dayStart,
                dayEnd);
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("邮箱验证码发送次数超限");
        }
        // 发送邮箱验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newSmsValidateCode(now, req.getDomainId(), req.getUid(), req.getEmail(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendEmail(EnumConstant.ValidateCode_Type_12, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_12);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_2);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendBindEmailValidateCodeRes(validateCode);
    }

    @Override
    public SendBindSmsValidateCodeRes sendBindSmsValidateCode(SendBindSmsValidateCodeReq req) {
        final Date now = new Date();
        final Date dayStart = DateTimeUtils.getDayStartTime(now);
        final Date dayEnd = DateTimeUtils.getDayEndTime(now);
        User user = userMapper.getByTelephone(req.getTelephone());
        if (user != null) {
            int exists = userDomainMapper.exists(req.getDomainId(), user.getUid());
            if (exists > 0) {
                throw new BusinessException("当前手机号已注册");
            }
        }
        // 上一次短信验证码有效就直接返回
        ValidateCode lastEffective = validateCodeMapper.getLastEffectiveNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_10,
                EnumConstant.ValidateCode_SendChannel_1,
                req.getTelephone()
        );
        if (lastEffective != null) {
            // 返回
            return ConvertUtils.convertToSendBindSmsValidateCodeRes(lastEffective);
        }
        int sendCount = validateCodeMapper.getSendCountNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_10,
                EnumConstant.ValidateCode_SendChannel_1,
                req.getTelephone(),
                dayStart,
                dayEnd);
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("短信验证码发送次数超限");
        }
        // 发送邮箱验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newSmsValidateCode(now, req.getDomainId(), req.getUid(), req.getTelephone(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendEmail(EnumConstant.ValidateCode_Type_10, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_10);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_1);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendBindSmsValidateCodeRes(validateCode);
    }

    @Override
    public VerifyBindEmailValidateCodeRes verifyBindEmailValidateCode(VerifyBindEmailValidateCodeReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCodeService.verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_12,
                EnumConstant.ValidateCode_SendChannel_2,
                req.getCode(),
                req.getCodeDigest(),
                req.getEmail());
        VerifyBindEmailValidateCodeRes res = new VerifyBindEmailValidateCodeRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public VerifyBindSmsValidateCodeRes verifyBindSmsValidateCode(VerifyBindSmsValidateCodeReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCodeService.verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_10,
                EnumConstant.ValidateCode_SendChannel_1,
                req.getCode(),
                req.getCodeDigest(),
                req.getTelephone());
        VerifyBindSmsValidateCodeRes res = new VerifyBindSmsValidateCodeRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public ChangeBindEmailRes changeBindEmail(ChangeBindEmailReq req) {
        User user = userMapper.getByEmail(req.getNewEmail());
        if (user != null) {
            throw new BusinessException("当前邮箱已经注册了");
        }
        user = verifyPassWord(req.getUid(), req.getPassWord());
        User update = new User();
        update.setUid(user.getUid());
        update.setEmail(req.getNewEmail());
        userMapper.updateById(update);
        // 清除 user_security_context
        userSecurityContextMapper.deleteByUid(user.getUid());
        // 返回
        ChangeBindEmailRes res = new ChangeBindEmailRes();
        res.setSuccess(true);
        return res;
    }

    @Override
    public ChangeBindSmsRes changeBindSms(ChangeBindSmsReq req) {
        User user = userMapper.getByTelephone(req.getNewTelephone());
        if (user != null) {
            throw new BusinessException("当前手机号已经注册了");
        }
        user = verifyPassWord(req.getUid(), req.getPassWord());
        User update = new User();
        update.setUid(req.getUid());
        update.setTelephone(req.getNewTelephone());
        userMapper.updateById(update);
        // 清除 user_security_context
        userSecurityContextMapper.deleteByUid(user.getUid());
        // 返回
        ChangeBindSmsRes res = new ChangeBindSmsRes();
        res.setSuccess(true);
        return res;
    }

    /**
     * 生成邮件换绑图片验证码
     *
     * @return {@code TupleFour<Code, Digest, image, ExpiredTime>}
     */
    protected TupleFour<String, String, String, Date> getBindCaptcha(Long domainId, Integer effectiveTimeMilli, int type) {
        final Date now = new Date();
        ValidateCode validateCode = ValidateCodeUtils.newCaptchaValidateCode(now, domainId, effectiveTimeMilli);
        validateCode.setType(type);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_0);
        byte[] image = ImageValidateCageUtils.createImage(validateCode.getCode());
        validateCodeMapper.insert(validateCode);
        return TupleFour.creat(validateCode.getCode(), validateCode.getDigest(), EncodeDecodeUtils.encodeBase64(image), validateCode.getExpiredTime());
    }

    /**
     * 密码验证
     *
     * @param uid      用户uid
     * @param passWord 密码
     * @return 用户信息
     */
    protected User verifyPassWord(String uid, String passWord) {
        User user = userMapper.getByUid(uid);
        if (user == null) {
            throw new BusinessException("密码错误");
        }
        if (!passwordEncoder.matches(passWord, user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        return user;
    }
}
