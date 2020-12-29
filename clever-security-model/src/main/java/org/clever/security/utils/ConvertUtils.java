package org.clever.security.utils;

import org.clever.security.dto.response.SendLoginValidateCodeForEmailRes;
import org.clever.security.dto.response.SendLoginValidateCodeForSmsRes;
import org.clever.security.entity.User;
import org.clever.security.entity.ValidateCode;
import org.clever.security.model.UserInfo;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/19 13:15 <br/>
 */
public class ConvertUtils {

    public static UserInfo convertToUserInfo(User user) {
        if (user == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(user.getUid());
        userInfo.setLoginName(user.getLoginName());
        userInfo.setPassword(user.getPassword());
        userInfo.setTelephone(user.getTelephone());
        userInfo.setEmail(user.getEmail());
        userInfo.setExpiredTime(user.getExpiredTime());
        userInfo.setEnabled(user.getEnabled());
        userInfo.getExtInfo().put("nickname", user.getNickname());
        userInfo.getExtInfo().put("avatar", user.getAvatar());
        userInfo.getExtInfo().put("registerChannel", user.getRegisterChannel());
        userInfo.getExtInfo().put("fromSource", user.getFromSource());
        userInfo.getExtInfo().put("description", user.getDescription());
        userInfo.getExtInfo().put("createAt", user.getCreateAt());
        userInfo.getExtInfo().put("updateAt", user.getUpdateAt());
        return userInfo;
    }

    public static SendLoginValidateCodeForSmsRes convertToSendLoginValidateCodeForSmsRes(ValidateCode validateCode) {
        SendLoginValidateCodeForSmsRes res = new SendLoginValidateCodeForSmsRes();
        res.setCode(validateCode.getCode());
        res.setDigest(validateCode.getDigest());
        res.setExpiredTime(validateCode.getExpiredTime());
        return res;
    }

    public static SendLoginValidateCodeForEmailRes convertToSendLoginValidateCodeForEmailRes(ValidateCode validateCode) {
        SendLoginValidateCodeForEmailRes res = new SendLoginValidateCodeForEmailRes();
        res.setCode(validateCode.getCode());
        res.setDigest(validateCode.getDigest());
        res.setExpiredTime(validateCode.getExpiredTime());
        return res;
    }
}
