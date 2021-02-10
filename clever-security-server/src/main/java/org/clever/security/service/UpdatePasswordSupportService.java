package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
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
    public GetInitPasswordCaptchaRes getInitPasswordCaptcha(GetInitPasswordCaptchaReq req) {
        final Date now = new Date();
        ValidateCode validateCode = ValidateCodeUtils.newCaptchaValidateCode(now, req.getDomainId(), req.getEffectiveTimeMilli());
        validateCode.setType(EnumConstant.ValidateCode_Type_3);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_0);
        byte[] image = ImageValidateCageUtils.createImage(validateCode.getCode());
        validateCodeMapper.insert(validateCode);
        GetInitPasswordCaptchaRes res = new GetInitPasswordCaptchaRes();
        res.setCode(validateCode.getCode());
        res.setDigest(validateCode.getDigest());
        res.setCodeContent(EncodeDecodeUtils.encodeBase64(image));
        res.setExpiredTime(validateCode.getExpiredTime());
        return res;
    }

    @Override
    public VerifyInitPasswordCaptchaRes verifyInitPasswordCaptcha(VerifyInitPasswordCaptchaReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCodeService.verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_3,
                EnumConstant.ValidateCode_SendChannel_0,
                req.getCaptcha(),
                req.getCaptchaDigest(),
                null);
        VerifyInitPasswordCaptchaRes res = new VerifyInitPasswordCaptchaRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public SendSmsInitPasswordValidateCodeRes sendSmsInitPasswordValidateCode(SendSmsInitPasswordValidateCodeReq req) {
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
                EnumConstant.ValidateCode_Type_3,
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
                EnumConstant.ValidateCode_Type_3,
                EnumConstant.ValidateCode_SendChannel_1,
                dayStart,
                dayEnd);
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("短信验证码发送次数超限");
        }
        // 发送短信验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newSmsValidateCode(now, req.getDomainId(), user.getUid(), req.getTelephone(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendEmail(EnumConstant.ValidateCode_Type_3, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_3);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_1);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendSmsUpdatePasswordValidateCodeRes(validateCode);
    }

    @Override
    public VerifySmsInitPasswordValidateCodeRes verifySmsInitPasswordValidateCode(VerifySmsInitPasswordValidateCodeReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCodeService.verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_3,
                EnumConstant.ValidateCode_SendChannel_1,
                req.getCode(),
                req.getCodeDigest(),
                req.getTelephone());
        VerifySmsInitPasswordValidateCodeRes res = new VerifySmsInitPasswordValidateCodeRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public SendEmailInitPasswordValidateCodeRes sendEmailInitPasswordValidateCode(SendEmailInitPasswordValidateCodeReq req) {
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
                EnumConstant.ValidateCode_Type_3,
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
                EnumConstant.ValidateCode_Type_3,
                EnumConstant.ValidateCode_SendChannel_2,
                dayStart,
                dayEnd);
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("邮箱验证码发送次数超限");
        }
        // 发送邮箱验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newEmailValidateCode(now, req.getDomainId(), user.getUid(), req.getEmail(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendEmail(EnumConstant.ValidateCode_Type_3, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_3);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_2);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendEmailUpdatePasswordValidateCodeRes(validateCode);
    }

    @Override
    public VerifyEmailInitPasswordValidateCodeRes verifyEmailInitPasswordValidateCode(VerifyEmailInitPasswordValidateCodeReq req) {
        TupleThree<Boolean, String, Boolean> tuple = verifyValidateCodeService.verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_3,
                EnumConstant.ValidateCode_SendChannel_2,
                req.getCode(),
                req.getCodeDigest(),
                req.getEmail());
        VerifyEmailInitPasswordValidateCodeRes res = new VerifyEmailInitPasswordValidateCodeRes();
        res.setSuccess(tuple.getValue1());
        res.setMessage(tuple.getValue2());
        res.setExpired(tuple.getValue3());
        return res;
    }

    @Override
    public InitPasswordRes initPassword(InitPasswordReq req) {
        User user;
        if (StringUtils.isNotBlank(req.getTelephone())) {
            user = userMapper.getByTelephone(req.getTelephone());
        } else if (StringUtils.isNotBlank(req.getEmail())) {
            user = userMapper.getByEmail(req.getEmail());
        } else {
            throw new BusinessException("邮箱和手机号不能同时为空");
        }
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (StringUtils.isNotBlank(user.getPassword())) {
            throw new BusinessException("已设置密码,无需再次设置");
        }
        updatePassword(user.getUid(), passwordEncoder.encode(req.getInitPassword()));
        return new InitPasswordRes(true);
    }

    @Override
    public UpdatePasswordRes updatePassword(UpdatePasswordReq req) {
        User user = userMapper.getByUid(req.getUid());
        if (user == null || !passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new BusinessException("密码不正确");
        }
        updatePassword(user.getUid(), passwordEncoder.encode(req.getNewPassword()));
        // 禁用当前用户的Token
        final String disableReason = "用户修改密码";
        jwtTokenMapper.disableJwtTokenByUid(user.getUid(), disableReason);
        return new UpdatePasswordRes(true);
    }

    protected void updatePassword(String uid, String password) {
        User update = new User();
        update.setUid(uid);
        update.setPassword(password);
        userMapper.updateById(update);
        // 清除 user_security_context
        userSecurityContextMapper.deleteByUid(update.getUid());
    }
}
