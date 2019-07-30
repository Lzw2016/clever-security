package org.clever.security.embed.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.codec.DigestUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
import org.clever.common.utils.imgvalidate.ValidateCodeSourceUtils;
import org.clever.common.utils.reflection.ReflectionsUtils;
import org.clever.security.Constant;
import org.clever.security.LoginModel;
import org.clever.security.config.SecurityConfig;
import org.clever.security.model.CaptchaInfo;
import org.clever.security.repository.CaptchaInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-19 21:21 <br/>
 */
@Api("验证码")
@RestController
@Slf4j
public class CaptchaController {

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private CaptchaInfoRepository captchaInfoRepository;

    @ApiOperation("获取登录验证码(请求头包含文件SHA1签名)")
    @GetMapping("/login/captcha.png")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = ValidateCodeSourceUtils.getRandString(4);
        byte[] image = ImageValidateCageUtils.createImage(code);
        // 3分钟有效
        Long captchaEffectiveTime = 180000L;
        String imageDigestHeader = "ImageDigest";
        if (securityConfig.getLogin() != null && securityConfig.getLogin().getCaptchaEffectiveTime() != null) {
            captchaEffectiveTime = securityConfig.getLogin().getCaptchaEffectiveTime().toMillis();
        }
        CaptchaInfo captchaInfo;
        setContentTypeNoCharset(response, "image/png");
        if (LoginModel.jwt.equals(securityConfig.getLoginModel())) {
            // JWT Token
            String imageDigest = EncodeDecodeUtils.encodeHex(DigestUtils.sha1(image));
            captchaInfo = new CaptchaInfo(code, captchaEffectiveTime, imageDigest);
            // 验证码放在Redis
            captchaInfoRepository.saveCaptchaInfo(captchaInfo);
            // 写入图片数据
            response.setHeader(imageDigestHeader, imageDigest);
            log.info("### 获取验证码[{}]", captchaInfo);
        } else {
            // Session
            captchaInfo = new CaptchaInfo(code, captchaEffectiveTime);
            request.getSession().setAttribute(Constant.Login_Captcha_Session_Key, captchaInfo);
            log.info("### 客户端[SessionID={}] 获取验证码[{}]", request.getSession().getId(), captchaInfo);
        }
        response.getOutputStream().write(image);
    }

    @SuppressWarnings("SameParameterValue")
    private static void setContentTypeNoCharset(HttpServletResponse response, String contentType) {
        boolean flag = false;
        Object object = response;
        while (true) {
            try {
                object = ReflectionsUtils.getFieldValue(object, "response");
            } catch (Throwable e) {
                break;
            }
            if (object instanceof org.apache.catalina.connector.Response) {
                break;
            }
        }
        if (object instanceof org.apache.catalina.connector.Response) {
            org.apache.catalina.connector.Response connectorResponse = (org.apache.catalina.connector.Response) object;
            object = ReflectionsUtils.getFieldValue(connectorResponse, "coyoteResponse");
            if (object instanceof org.apache.coyote.Response) {
                org.apache.coyote.Response coyoteResponse = (org.apache.coyote.Response) object;
                coyoteResponse.setContentTypeNoCharset(contentType);
                ReflectionsUtils.setFieldValue(coyoteResponse, "charset", null);
                flag = true;
            }
        }
        if (!flag) {
            response.setContentType(contentType);
        }
    }
}
