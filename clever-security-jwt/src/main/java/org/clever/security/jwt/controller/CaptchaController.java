package org.clever.security.jwt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.codec.DigestUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
import org.clever.common.utils.imgvalidate.ValidateCodeSourceUtils;
import org.clever.security.jwt.config.SecurityConfig;
import org.clever.security.jwt.model.CaptchaInfo;
import org.clever.security.jwt.repository.CaptchaInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-19 21:21 <br/>
 */
@SuppressWarnings("Duplicates")
@Api(description = "验证码")
@RestController
@Slf4j
public class CaptchaController {

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private CaptchaInfoRepository captchaInfoRepository;

    @ApiOperation("获取登录验证码(请求头包含文件SHA1签名)")
    @GetMapping("/login/captcha.png")
    public void captcha(HttpServletResponse response) throws IOException {
        String code = ValidateCodeSourceUtils.getRandString(4);
        byte[] image = ImageValidateCageUtils.createImage(code);
        Long captchaEffectiveTime = 60000L;
        if (securityConfig.getLogin() != null && securityConfig.getLogin().getCaptchaEffectiveTime() != null) {
            captchaEffectiveTime = securityConfig.getLogin().getCaptchaEffectiveTime();
        }
        String imageDigest = EncodeDecodeUtils.encodeHex(DigestUtils.sha1(image));
        CaptchaInfo captchaInfo = new CaptchaInfo(code, captchaEffectiveTime, imageDigest);
        // 验证码放在Redis
        captchaInfoRepository.saveCaptchaInfo(captchaInfo);
        // 写入图片数据
        response.setHeader("ImageDigest", imageDigest);
        response.getOutputStream().write(image);
        log.info("### 获取验证码[{}]", captchaInfo);
    }
}
