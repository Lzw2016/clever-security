package org.clever.security.service;

import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
import org.clever.common.utils.tuples.TupleFour;
import org.clever.common.utils.tuples.TupleThree;
import org.clever.security.client.BindSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.ValidateCode;
import org.clever.security.mapper.UserMapper;
import org.clever.security.mapper.ValidateCodeMapper;
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
    private ValidateCodeMapper validateCodeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RegisterSupportService registerSupportService;

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
        TupleThree<Boolean, String, Boolean> tuple = registerSupportService.verifyValidateCode(
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
        TupleThree<Boolean, String, Boolean> tuple = registerSupportService.verifyValidateCode(
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
        return null;
    }

    @Override
    public SendBindSmsValidateCodeRes sendBindSmsValidateCode(SendBindSmsValidateCodeReq req) {
        return null;
    }

    @Override
    public VerifyBindEmailValidateCodeRes verifyBindEmailValidateCode(VerifyBindEmailValidateCodeReq req) {
        TupleThree<Boolean, String, Boolean> tuple = registerSupportService.verifyValidateCode(
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
        TupleThree<Boolean, String, Boolean> tuple = registerSupportService.verifyValidateCode(
                req.getDomainId(),
                EnumConstant.ValidateCode_Type_10,
                EnumConstant.ValidateCode_SendChannel_2,
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
        return null;
    }

    @Override
    public ChangeBindSmsRes changeBindSms(ChangeBindSmsReq req) {
        return null;
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
}
