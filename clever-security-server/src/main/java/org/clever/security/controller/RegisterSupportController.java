package org.clever.security.controller;

import org.clever.security.client.RegisterSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.model.register.EmailRegisterReq;
import org.clever.security.model.register.LoginNameRegisterReq;
import org.clever.security.model.register.SmsRegisterReq;
import org.clever.security.service.RegisterSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 17:24 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class RegisterSupportController implements RegisterSupportClient {
    @Autowired
    private RegisterSupportService registerSupportService;

    /**
     * 获取登录名注册-验证码
     */
    @GetMapping("/login_name_register_captcha")
    @Override
    public GetLoginNameRegisterCaptchaRes getLoginNameRegisterCaptcha(@Validated GetLoginNameRegisterCaptchaReq req) {
        return registerSupportService.getLoginNameRegisterCaptcha(req);
    }

    /**
     * 验证登录名注册-验证码
     */
    @PostMapping("/verify_login_name_register_captcha")
    @Override
    public VerifyLoginNameRegisterCaptchaRes verifyLoginNameRegisterCaptcha(@Validated @RequestBody VerifyLoginNameRegisterCaptchaReq req) {
        return registerSupportService.verifyLoginNameRegisterCaptcha(req);
    }

    /**
     * 获取短信注册-图片验证码
     */
    @GetMapping("/sms_register_captcha")
    @Override
    public GetSmsRegisterCaptchaRes getSmsRegisterCaptcha(@Validated GetSmsRegisterCaptchaReq req) {
        return registerSupportService.getSmsRegisterCaptcha(req);
    }

    /**
     * 验证短信注册-图片验证码
     */
    @PostMapping("/verify_sms_register_captcha")
    @Override
    public VerifySmsRegisterCaptchaRes verifySmsRegisterCaptcha(@Validated @RequestBody VerifySmsRegisterCaptchaReq req) {
        return registerSupportService.verifySmsRegisterCaptcha(req);
    }

    /**
     * 发送短信注册-验证码
     */
    @PostMapping("/sms_register_send_validate_code")
    @Override
    public SendSmsValidateCodeRes sendSmsValidateCode(@Validated @RequestBody SendSmsValidateCodeReq req) {
        return registerSupportService.sendSmsValidateCode(req);
    }

    /**
     * 验证短信注册-短信验证码
     */
    @PostMapping("/verify_sms_register_send_validate_code")
    @Override
    public VerifySmsValidateCodeRes verifySmsValidateCode(@Validated @RequestBody VerifySmsValidateCodeReq req) {
        return registerSupportService.verifySmsValidateCode(req);
    }

    /**
     * 获取邮箱注册-图片验证码
     */
    @GetMapping("/email_register_captcha")
    @Override
    public GetEmailRegisterCaptchaRes getEmailRegisterCaptcha(@Validated GetEmailRegisterCaptchaReq req) {
        return registerSupportService.getEmailRegisterCaptcha(req);
    }

    /**
     * 验证短信注册-图片验证码
     */
    @PostMapping("/verify_email_register_captcha")
    @Override
    public VerifyEmailRegisterCaptchaRes verifyEmailRegisterCaptcha(@Validated @RequestBody VerifyEmailRegisterCaptchaReq req) {
        return registerSupportService.verifyEmailRegisterCaptcha(req);
    }

    /**
     * 发送邮箱注册-邮箱验证码
     */
    @PostMapping("/email_register_send_validate_code")
    @Override
    public SendEmailValidateCodeRes sendEmailValidateCode(@Validated @RequestBody SendEmailValidateCodeReq req) {
        return registerSupportService.sendEmailValidateCode(req);
    }

    /**
     * 验证邮箱注册-邮箱验证码
     */
    @PostMapping("/verify_email_register_send_validate_code")
    @Override
    public VerifyEmailValidateCodeRes verifyEmailValidateCode(@Validated @RequestBody VerifyEmailValidateCodeReq req) {
        return registerSupportService.verifyEmailValidateCode(req);
    }

    /**
     * 根据登录名注册
     */
    @PostMapping("/register_by_login_name")
    @Override
    public UserRegisterRes registerByLoginName(@Validated @RequestBody LoginNameRegisterReq req) {
        return registerSupportService.registerByLoginName(req);
    }

    /**
     * 根据邮箱注册
     */
    @PostMapping("/register_by_email")
    @Override
    public UserRegisterRes registerByEmail(@Validated @RequestBody EmailRegisterReq req) {
        return registerSupportService.registerByEmail(req);
    }

    /**
     * 根据短信注册
     */
    @PostMapping("/register_by_sms")
    @Override
    public UserRegisterRes registerBySms(@Validated @RequestBody SmsRegisterReq req) {
        return registerSupportService.registerBySms(req);
    }
}
