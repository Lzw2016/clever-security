package org.clever.security.utils;

import org.clever.security.entity.User;
import org.clever.security.model.UserInfo;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/19 13:15 <br/>
 */
public class ConvertUtils {

    public static UserInfo convertToUserInfo(User user) {
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
}
