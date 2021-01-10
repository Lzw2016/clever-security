package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
import org.clever.common.utils.tuples.TupleTow;
import org.clever.security.client.RegisterSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.User;
import org.clever.security.entity.ValidateCode;
import org.clever.security.mapper.DomainMapper;
import org.clever.security.mapper.UserDomainMapper;
import org.clever.security.mapper.UserMapper;
import org.clever.security.mapper.ValidateCodeMapper;
import org.clever.security.model.register.EmailRegisterReq;
import org.clever.security.model.register.LoginNameRegisterReq;
import org.clever.security.model.register.SmsRegisterReq;
import org.clever.security.utils.ConvertUtils;
import org.clever.security.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 17:25 <br/>
 */
@Transactional
@Primary
@Service
@Slf4j
public class RegisterSupportService implements RegisterSupportClient {
    @Autowired
    private DomainMapper domainMapper;
    @Autowired
    private ValidateCodeMapper validateCodeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDomainMapper userDomainMapper;
    @Autowired
    private SendValidateCodeService sendValidateCodeService;

    @Override
    public GetLoginNameRegisterCaptchaRes getLoginNameRegisterCaptcha(GetLoginNameRegisterCaptchaReq req) {
        final Date now = new Date();
        ValidateCode validateCode = ValidateCodeUtils.newCaptchaValidateCode(now, req.getDomainId(), req.getEffectiveTimeMilli());
        validateCode.setType(EnumConstant.ValidateCode_Type_4);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_0);
        byte[] image = ImageValidateCageUtils.createImage(validateCode.getCode());
        validateCodeMapper.insert(validateCode);
        GetLoginNameRegisterCaptchaRes res = new GetLoginNameRegisterCaptchaRes();
        res.setCode(validateCode.getCode());
        res.setDigest(validateCode.getDigest());
        res.setCodeContent(EncodeDecodeUtils.encodeBase64(image));
        res.setExpiredTime(validateCode.getExpiredTime());
        return res;
    }

    @Override
    public VerifyLoginNameRegisterCaptchaRes verifyLoginNameRegisterCaptcha(VerifyLoginNameRegisterCaptchaReq req) {
        TupleTow<Boolean, String> tupleTow = verifyValidateCode(req.getDomainId(), EnumConstant.ValidateCode_Type_4, EnumConstant.ValidateCode_SendChannel_0, req.getCaptcha(), req.getCaptchaDigest());
        VerifyLoginNameRegisterCaptchaRes res = new VerifyLoginNameRegisterCaptchaRes();
        res.setSuccess(tupleTow.getValue1());
        res.setMessage(tupleTow.getValue2());
        return res;
    }

    @Override
    public GetSmsRegisterCaptchaRes getSmsRegisterCaptcha(GetSmsRegisterCaptchaReq req) {
        final Date now = new Date();
        ValidateCode validateCode = ValidateCodeUtils.newCaptchaValidateCode(now, req.getDomainId(), req.getEffectiveTimeMilli());
        validateCode.setType(EnumConstant.ValidateCode_Type_5);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_0);
        byte[] image = ImageValidateCageUtils.createImage(validateCode.getCode());
        validateCodeMapper.insert(validateCode);
        GetSmsRegisterCaptchaRes res = new GetSmsRegisterCaptchaRes();
        res.setCode(validateCode.getCode());
        res.setDigest(validateCode.getDigest());
        res.setCodeContent(EncodeDecodeUtils.encodeBase64(image));
        res.setExpiredTime(validateCode.getExpiredTime());
        return res;
    }

    @Override
    public VerifySmsRegisterCaptchaRes verifySmsRegisterCaptcha(VerifySmsRegisterCaptchaReq req) {
        TupleTow<Boolean, String> tupleTow = verifyValidateCode(req.getDomainId(), EnumConstant.ValidateCode_Type_5, EnumConstant.ValidateCode_SendChannel_0, req.getCaptcha(), req.getCaptchaDigest());
        VerifySmsRegisterCaptchaRes res = new VerifySmsRegisterCaptchaRes();
        res.setSuccess(tupleTow.getValue1());
        res.setMessage(tupleTow.getValue2());
        return res;
    }

    @Override
    public SendSmsValidateCodeRes sendSmsValidateCode(SendSmsValidateCodeReq req) {
        final Date now = new Date();
        final Date dayStart = DateTimeUtils.getDayStartTime(now);
        final Date dayEnd = DateTimeUtils.getDayEndTime(now);
        User user = userMapper.getByTelephone(req.getTelephone());
        if (user != null) {
            int exists = userDomainMapper.exists(req.getDomainId(), user.getUid());
            if (exists > 0) {
                throw new BusinessException("当前手机号已经注册了");
            }
        }
        // 上一次短信验证码有效就直接返回
        ValidateCode lastEffective = validateCodeMapper.getLastEffectiveNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_6,
                EnumConstant.ValidateCode_SendChannel_1,
                req.getTelephone()
        );
        if (lastEffective != null) {
            // 返回
            return ConvertUtils.convertToSendSmsValidateCodeRes(lastEffective);
        }
        int sendCount = validateCodeMapper.getSendCountNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_6,
                EnumConstant.ValidateCode_SendChannel_1,
                req.getTelephone(),
                dayStart,
                dayEnd
        );
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("短信验证码发送次数超限");
        }
        // 发送短信验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newSmsValidateCode(now, req.getDomainId(), null, req.getTelephone(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendSms(EnumConstant.ValidateCode_Type_6, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_6);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_1);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendSmsValidateCodeRes(validateCode);
    }

    @Override
    public VerifySmsValidateCodeRes verifySmsValidateCode(VerifySmsValidateCodeReq req) {
        TupleTow<Boolean, String> tupleTow = verifyValidateCode(req.getDomainId(), EnumConstant.ValidateCode_Type_6, EnumConstant.ValidateCode_SendChannel_1, req.getCode(), req.getCodeDigest());
        VerifySmsValidateCodeRes res = new VerifySmsValidateCodeRes();
        res.setSuccess(tupleTow.getValue1());
        res.setMessage(tupleTow.getValue2());
        return res;
    }

    @Override
    public GetEmailRegisterCaptchaRes getEmailRegisterCaptcha(GetEmailRegisterCaptchaReq req) {
        final Date now = new Date();
        ValidateCode validateCode = ValidateCodeUtils.newCaptchaValidateCode(now, req.getDomainId(), req.getEffectiveTimeMilli());
        validateCode.setType(EnumConstant.ValidateCode_Type_7);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_0);
        byte[] image = ImageValidateCageUtils.createImage(validateCode.getCode());
        validateCodeMapper.insert(validateCode);
        GetEmailRegisterCaptchaRes res = new GetEmailRegisterCaptchaRes();
        res.setCode(validateCode.getCode());
        res.setDigest(validateCode.getDigest());
        res.setCodeContent(EncodeDecodeUtils.encodeBase64(image));
        res.setExpiredTime(validateCode.getExpiredTime());
        return res;
    }

    @Override
    public VerifyEmailRegisterCaptchaRes verifyEmailRegisterCaptcha(VerifyEmailRegisterCaptchaReq req) {
        TupleTow<Boolean, String> tupleTow = verifyValidateCode(req.getDomainId(), EnumConstant.ValidateCode_Type_7, EnumConstant.ValidateCode_SendChannel_0, req.getCaptcha(), req.getCaptchaDigest());
        VerifyEmailRegisterCaptchaRes res = new VerifyEmailRegisterCaptchaRes();
        res.setSuccess(tupleTow.getValue1());
        res.setMessage(tupleTow.getValue2());
        return res;
    }

    protected TupleTow<Boolean, String> verifyValidateCode(long domainId, int type, int sendChannel, String captcha, String captchaDigest) {
        final Date now = new Date();
        ValidateCode validateCode = validateCodeMapper.getByDigest(domainId, type, sendChannel, captchaDigest);
        TupleTow<Boolean, String> tupleTow = ValidateCodeUtils.verifyValidateCode(validateCode, now, captcha);
        // 更新验证码
        if (validateCode != null && validateCode.getValidateTime() == null) {
            ValidateCode update = new ValidateCode();
            update.setId(validateCode.getId());
            update.setValidateTime(now);
            validateCodeMapper.updateById(update);
        }
        return tupleTow;
    }

    @Override
    public SendEmailValidateCodeRes sendEmailValidateCode(SendEmailValidateCodeReq req) {
        final Date now = new Date();
        final Date dayStart = DateTimeUtils.getDayStartTime(now);
        final Date dayEnd = DateTimeUtils.getDayEndTime(now);
        User user = userMapper.getByEmail(req.getEmail());
        if (user != null) {
            int exists = userDomainMapper.exists(req.getDomainId(), user.getUid());
            if (exists > 0) {
                throw new BusinessException("当前邮箱已经注册了");
            }
        }
        // 上一次邮箱验证码有效就直接返回
        ValidateCode lastEffective = validateCodeMapper.getLastEffectiveNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_8,
                EnumConstant.ValidateCode_SendChannel_2,
                req.getEmail()
        );
        if (lastEffective != null) {
            // 返回
            return ConvertUtils.convertToSendEmailValidateCodeRes(lastEffective);
        }
        int sendCount = validateCodeMapper.getSendCountNoUid(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_8,
                EnumConstant.ValidateCode_SendChannel_2,
                req.getEmail(),
                dayStart,
                dayEnd
        );
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("邮件验证码发送次数超限");
        }
        // 发送邮件验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newEmailValidateCode(now, req.getDomainId(), null, req.getEmail(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendEmail(EnumConstant.ValidateCode_Type_8, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_8);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_2);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendEmailValidateCodeRes(validateCode);
    }

    @Override
    public UserRegisterRes registerByLoginName(LoginNameRegisterReq req) {
        return null;
    }

    @Override
    public UserRegisterRes registerByEmail(EmailRegisterReq req) {
        return null;
    }

    @Override
    public UserRegisterRes registerBySms(SmsRegisterReq req) {
        return null;
    }
}
