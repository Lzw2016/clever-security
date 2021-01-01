package org.clever.security.utils;

import org.clever.common.utils.IDCreateUtils;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.imgvalidate.ValidateCodeSourceUtils;
import org.clever.security.entity.ValidateCode;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/29 19:42 <br/>
 */
public class ValidateCodeUtils {

    public static ValidateCode newValidateCode(Date now, long domainId, String uid, String sendTarget, int effectiveTimeMilli) {
        String code = ValidateCodeSourceUtils.getRandString(6);
        ValidateCode validateCode = new ValidateCode();
        validateCode.setId(SnowFlake.SNOW_FLAKE.nextId());
        validateCode.setDomainId(domainId);
        validateCode.setUid(uid);
        validateCode.setCode(code);
        validateCode.setDigest(IDCreateUtils.uuid());
        validateCode.setSendTarget(sendTarget);
        validateCode.setExpiredTime(new Date(now.getTime() + effectiveTimeMilli));
        return validateCode;
    }
}
