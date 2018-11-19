//package org.clever.security.jwt.event;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.clever.common.utils.mapper.JacksonMapper;
//import org.clever.security.client.UserLoginLogClient;
//import org.clever.security.dto.request.UserLoginLogAddReq;
//import org.clever.security.entity.EnumConstant;
//import org.clever.security.jwt.config.SecurityConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.WebAuthenticationDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//
///**
// * 登录事件监听
// * 作者： lzw<br/>
// * 创建时间：2018-09-18 13:55 <br/>
// */
//@SuppressWarnings("Duplicates")
//@Transactional(readOnly = true)
//@Component
//@Slf4j
//public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
//
//    @Autowired
//    private SecurityConfig securityConfig;
//    @Autowired
//    private UserLoginLogClient userLoginLogClient;
//
//    @Transactional
//    @Override
//    public void onApplicationEvent(AuthenticationSuccessEvent event) {
//        Authentication authentication = event.getAuthentication();
//        String loginIp = null;
//        String sessionId = null;
//        if (authentication.getDetails() != null && authentication.getDetails() instanceof WebAuthenticationDetails) {
//            WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
//            loginIp = webAuthenticationDetails.getRemoteAddress();
//            sessionId = webAuthenticationDetails.getSessionId();
//        } else {
//            log.warn("### 登录成功未能得到SessionID [{}]", authentication.getName());
//        }
//        UserLoginLogAddReq userLoginLog = new UserLoginLogAddReq();
//        userLoginLog.setSysName(securityConfig.getSysName());
//        userLoginLog.setUsername(authentication.getName());
//        userLoginLog.setLoginTime(new Date());
//        userLoginLog.setLoginIp(StringUtils.trimToEmpty(loginIp));
//        userLoginLog.setAuthenticationInfo(JacksonMapper.nonEmptyMapper().toJson(authentication));
//        userLoginLog.setSessionId(StringUtils.trimToEmpty(sessionId));
//        userLoginLog.setLoginState(EnumConstant.UserLoginLog_LoginState_1);
//        try {
//            userLoginLogClient.addUserLoginLog(userLoginLog);
//            log.info("### 写入登录成功日志 [{}]", authentication.getName());
//        } catch (Exception e) {
//            log.error("写入登录成功日志失败 [{}]", authentication.getName(), e);
//        }
//    }
//}
