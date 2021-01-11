package org.clever.security.utils;

import org.clever.common.utils.IDCreateUtils;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.imgvalidate.ValidateCodeSourceUtils;
import org.clever.common.utils.tuples.TupleTow;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.ValidateCode;

import java.util.Date;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/29 19:42 <br/>
 */
public class ValidateCodeUtils {
    /**
     * 短信验证码字符
     */
    private static final char[] SMS_CODE_SEQ = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 新增图片验证码
     */
    public static ValidateCode newCaptchaValidateCode(Date now, long domainId, int effectiveTimeMilli) {
        ValidateCode validateCode = newValidateCode(now, domainId, null, null, effectiveTimeMilli);
        String code = ValidateCodeSourceUtils.getRandString(4);
        validateCode.setCode(code);
        return validateCode;
    }

    /**
     * 新增邮件验证码
     */
    public static ValidateCode newEmailValidateCode(Date now, long domainId, String uid, String sendTarget, int effectiveTimeMilli) {
        ValidateCode validateCode = newValidateCode(now, domainId, uid, sendTarget, effectiveTimeMilli);
        String code = ValidateCodeSourceUtils.getRandString(6);
        validateCode.setCode(code);
        return validateCode;
    }

    /**
     * 新增短信验证码
     */
    public static ValidateCode newSmsValidateCode(Date now, long domainId, String uid, String sendTarget, int effectiveTimeMilli) {
        ValidateCode validateCode = newValidateCode(now, domainId, uid, sendTarget, effectiveTimeMilli);
        String code = ValidateCodeSourceUtils.getRandString(6, SMS_CODE_SEQ);
        validateCode.setCode(code);
        return validateCode;
    }

    /**
     * 新增验证码
     */
    protected static ValidateCode newValidateCode(Date now, long domainId, String uid, String sendTarget, int effectiveTimeMilli) {
        ValidateCode validateCode = new ValidateCode();
        validateCode.setId(SnowFlake.SNOW_FLAKE.nextId());
        validateCode.setDomainId(domainId);
        validateCode.setUid(uid);
        validateCode.setDigest(IDCreateUtils.uuid());
        validateCode.setSendTarget(sendTarget);
        validateCode.setExpiredTime(new Date(now.getTime() + effectiveTimeMilli));
        return validateCode;
    }

    /**
     * 验证验证码
     */
    public static TupleTow<Boolean, String> verifyValidateCode(ValidateCode validateCode, Date now, String captcha, Integer type, Integer sendChannel, String sendTarget) {
        TupleTow<Boolean, String> tupleTow = verifyValidateCode(validateCode, now, type, sendChannel, sendTarget);
        if (tupleTow.getValue1()) {
            if (!Objects.equals(validateCode.getCode(), captcha)) {
                tupleTow.setValue1(false);
                tupleTow.setValue2("验证码错误");
            }
        }
        return tupleTow;
    }

    /**
     * 验证验证码
     */
    public static TupleTow<Boolean, String> verifyValidateCode(ValidateCode validateCode, Date now, String captcha, Integer type) {
        TupleTow<Boolean, String> tupleTow = verifyValidateCode(validateCode, now, type);
        if (tupleTow.getValue1()) {
            if (!Objects.equals(validateCode.getCode(), captcha)) {
                tupleTow.setValue1(false);
                tupleTow.setValue2("验证码错误");
            }
        }
        return tupleTow;
    }

    /**
     * 验证验证码
     */
    public static TupleTow<Boolean, String> verifyValidateCode(ValidateCode validateCode, Date now, Integer type, Integer sendChannel, String sendTarget) {
        String message = null;
        boolean success = false;
        if (validateCode == null) {
            message = "验证码错误";
        } else if (validateCode.getValidateTime() != null || now.compareTo(validateCode.getExpiredTime()) >= 0) {
            message = "验证码已过期";
        } else if (!Objects.equals(validateCode.getType(), type) || !Objects.equals(validateCode.getSendChannel(), sendChannel) || !Objects.equals(validateCode.getSendTarget(), sendTarget)) {
            message = "验证码错误";
        } else {
            success = true;
        }
        return TupleTow.creat(success, message);
    }

    /**
     * 验证验证码
     */
    public static TupleTow<Boolean, String> verifyValidateCode(ValidateCode validateCode, Date now, Integer type) {
        String message = null;
        boolean success = false;
        if (validateCode == null) {
            message = "验证码错误";
        } else if (validateCode.getValidateTime() != null || now.compareTo(validateCode.getExpiredTime()) >= 0) {
            message = "验证码已过期";
        } else if (!Objects.equals(validateCode.getType(), type) || !Objects.equals(validateCode.getSendChannel(), EnumConstant.ValidateCode_SendChannel_0)) {
            message = "验证码错误";
        } else {
            success = true;
        }
        return TupleTow.creat(success, message);
    }
}
