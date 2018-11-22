package org.clever.security.service;

import org.springframework.security.core.context.SecurityContext;

import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-22 19:32 <br/>
 */
public interface ISessionService {

    /**
     * 读取用户SessionSecurityContext
     *
     * @param sysName  系统名
     * @param userName 用户名
     * @return 不存在返回null
     */
    Map<String, SecurityContext> getSessionSecurityContext(String sysName, String userName);

    /**
     * 读取用户SessionSecurityContext
     *
     * @param sessionId Session ID
     */
    SecurityContext getSessionSecurityContext(String sessionId);

    /**
     * 重新加载所有系统用户权限信息(sessionAttr:SPRING_SECURITY_CONTEXT)
     *
     * @param sysName  系统名
     * @param userName 用户名
     */
    SecurityContext reloadSessionSecurityContext(String sysName, String userName);

    /**
     * 重新加载所有系统用户权限信息(sessionAttr:SPRING_SECURITY_CONTEXT)
     *
     * @param userName 用户名
     */
    Map<String, SecurityContext> reloadSessionSecurityContext(String userName);

    /**
     * 踢出用户(强制下线)
     *
     * @param sysName  系统名
     * @param userName 用户名
     * @return 下线Session数量
     */
    int forcedOffline(String sysName, String userName);

    /**
     * 删除用户Session(所有的系统)
     *
     * @param userName 用户信息
     */
    void delSession(String userName);

    /**
     * 删除用户Session
     *
     * @param sysName  系统名
     * @param userName 用户名
     */
    void delSession(String sysName, String userName);
}
