package org.clever.security.authentication;

import org.clever.security.client.ManageByUserClient;
import org.clever.security.client.UserClient;
import org.clever.security.model.LoginUserDetails;
import org.clever.security.token.login.BaseLoginToken;

/**
 * 加载第三方用户信息
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2019-04-28 18:55 <br/>
 */
public interface LoadThirdUser {

    /**
     * 是否支持当前loginToken从第三方加载用户信息
     *
     * @param loginToken 当前登录Token
     * @return 支持返回true
     */
    boolean supports(BaseLoginToken loginToken);

    /**
     * 从第三方系统加载用户信息
     *
     * @param loginToken         当前登录Token
     * @param userClient         读取系统用户
     * @param manageByUserClient 新增系统用户
     * @return 如仍需从权限系统读取用户信息则返回null，如果不需要则返回LoginUserDetails实体(建议把三方系统用户同步到当前系统并返回null)
     */
    LoginUserDetails loadUser(BaseLoginToken loginToken, UserClient userClient, ManageByUserClient manageByUserClient);
}
