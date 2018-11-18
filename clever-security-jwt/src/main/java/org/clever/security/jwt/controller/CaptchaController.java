//package org.clever.security.jwt.controller;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
//import org.clever.security.AttributeKeyConstant;
//import org.clever.security.config.SecurityConfig;
//import org.clever.security.model.CaptchaInfo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 作者： lzw<br/>
// * 创建时间：2018-09-19 21:21 <br/>
// */
//@Api(description = "验证码")
//@RestController
//@Slf4j
//public class CaptchaController {
//
//    @Autowired
//    private SecurityConfig securityConfig;
//
//    @ApiOperation("获取登录验证码")
//    @GetMapping("/login/captcha.png")
//    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String code = ImageValidateCageUtils.createImageStream(response.getOutputStream());
//        Long captchaEffectiveTime = 60000L;
//        if (securityConfig.getLogin() != null && securityConfig.getLogin().getCaptchaEffectiveTime() != null) {
//            captchaEffectiveTime = securityConfig.getLogin().getCaptchaEffectiveTime();
//
//        }
//        CaptchaInfo captchaInfo = new CaptchaInfo(code, captchaEffectiveTime);
//        request.getSession().setAttribute(AttributeKeyConstant.Login_Captcha_Session_Key, captchaInfo);
//        log.info("### 客户端[SessionID={}] 获取验证码[{}]", request.getSession().getId(), captchaInfo);
//    }
//}
