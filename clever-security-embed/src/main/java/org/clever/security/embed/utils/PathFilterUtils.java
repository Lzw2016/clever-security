package org.clever.security.embed.utils;

import org.apache.commons.lang3.StringUtils;
import org.clever.security.embed.config.SecurityConfig;
import org.clever.security.embed.config.internal.*;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/06 12:14 <br/>
 */
public class PathFilterUtils {
    private static final AntPathMatcher Ant_Path_Matcher = new AntPathMatcher();

    private static String getPath(HttpServletRequest request) {
        // request.getRequestURI()  /a/b/c/xxx.jsp
        // request.getContextPath() /a
        // request.getServletPath() /b/c/xxx.jsp
        return request.getRequestURI();
    }

    private static ScanCodeLoginConfig getScanCodeLoginConfig(SecurityConfig securityConfig) {
        LoginConfig login = securityConfig.getLogin();
        if (login == null) {
            return null;
        }
        ScanCodeLoginConfig scanCodeLogin = login.getScanCodeLogin();
        if (scanCodeLogin == null || !scanCodeLogin.isEnable()) {
            return null;
        }
        return scanCodeLogin;
    }

    /**
     * 当前请求是否是获取图片验证码请求
     */
    public static boolean isLoginCaptchaPath(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isLoginCaptchaPath(path, securityConfig);
    }

    public static boolean isLoginCaptchaPath(String path, SecurityConfig securityConfig) {
        LoginConfig login = securityConfig.getLogin();
        if (login == null) {
            return false;
        }
        LoginCaptchaConfig loginCaptcha = login.getLoginCaptcha();
        if (loginCaptcha == null || !loginCaptcha.isNeedCaptcha()) {
            return false;
        }
        return Objects.equals(loginCaptcha.getLoginCaptchaPath(), path);
    }

    /**
     * 当前请求是否是获取手机验证码请求
     */
    public static boolean isLoginSmsValidateCodePath(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isLoginSmsValidateCodePath(path, securityConfig);
    }

    public static boolean isLoginSmsValidateCodePath(String path, SecurityConfig securityConfig) {
        LoginConfig login = securityConfig.getLogin();
        if (login == null) {
            return false;
        }
        SmsValidateCodeLoginConfig smsValidateCodeLogin = login.getSmsValidateCodeLogin();
        if (smsValidateCodeLogin == null || !smsValidateCodeLogin.isEnable()) {
            return false;
        }
        return Objects.equals(smsValidateCodeLogin.getLoginSmsValidateCodePath(), path);
    }

    /**
     * 当前请求是否是获取邮箱验证码请求
     */
    public static boolean isLoginEmailValidateCodePath(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isLoginEmailValidateCodePath(path, securityConfig);
    }

    public static boolean isLoginEmailValidateCodePath(String path, SecurityConfig securityConfig) {
        LoginConfig login = securityConfig.getLogin();
        if (login == null) {
            return false;
        }
        EmailValidateCodeLoginConfig emailValidateCodeLogin = login.getEmailValidateCodeLogin();
        if (emailValidateCodeLogin == null || !emailValidateCodeLogin.isEnable()) {
            return false;
        }
        return Objects.equals(emailValidateCodeLogin.getLoginEmailValidateCodePath(), path);
    }

    /**
     * 当前请求是否是获取扫码登录二维码请求
     */
    public static boolean isGetScanCodeLoginPath(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isGetScanCodeLoginPath(path, securityConfig);
    }

    public static boolean isGetScanCodeLoginPath(String path, SecurityConfig securityConfig) {
        ScanCodeLoginConfig scanCodeLogin = getScanCodeLoginConfig(securityConfig);
        if (scanCodeLogin == null) {
            return false;
        }
        return Objects.equals(scanCodeLogin.getGetScanCodeLoginPath(), path);
    }

    /**
     * 当前请求是否是获取登录二维码状态请求
     */
    public static boolean isScanCodeStatePath(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isScanCodeStatePath(path, securityConfig);
    }

    public static boolean isScanCodeStatePath(String path, SecurityConfig securityConfig) {
        ScanCodeLoginConfig scanCodeLogin = getScanCodeLoginConfig(securityConfig);
        if (scanCodeLogin == null) {
            return false;
        }
        return Objects.equals(scanCodeLogin.getScanCodeStatePath(), path);
    }

    /**
     * 当前请求是否是扫描登录二维码请求
     */
    public static boolean isScanCodePath(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isScanCodePath(path, securityConfig);
    }

    public static boolean isScanCodePath(String path, SecurityConfig securityConfig) {
        ScanCodeLoginConfig scanCodeLogin = getScanCodeLoginConfig(securityConfig);
        if (scanCodeLogin == null) {
            return false;
        }
        return Objects.equals(scanCodeLogin.getScanCodePath(), path);
    }

    /**
     * 当前请求是否是扫码登录确认登录请求
     */
    public static boolean isScanCodeLoginConfirmPath(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isScanCodeLoginConfirmPath(path, securityConfig);
    }

    public static boolean isScanCodeLoginConfirmPath(String path, SecurityConfig securityConfig) {
        ScanCodeLoginConfig scanCodeLogin = getScanCodeLoginConfig(securityConfig);
        if (scanCodeLogin == null) {
            return false;
        }
        return Objects.equals(scanCodeLogin.getConfirmLoginPath(), path);
    }

    /**
     * 当前请求是否是登录请求
     */
    public static boolean isLoginRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isLoginRequest(path, request.getMethod(), securityConfig);
    }

    public static boolean isLoginRequest(String path, String method, SecurityConfig securityConfig) {
        LoginConfig login = securityConfig.getLogin();
        if (login == null) {
            return false;
        }
        if (!Objects.equals(login.getLoginPath(), path)) {
            return false;
        }
        boolean postRequest = StringUtils.isBlank(method) || HttpMethod.POST.matches(method);
        return !login.isPostOnly() || postRequest;
    }

    /**
     * 当前请求是否是登录请求
     */
    public static boolean isLogoutRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isLogoutRequest(path, securityConfig);
    }

    public static boolean isLogoutRequest(String path, SecurityConfig securityConfig) {
        LogoutConfig logout = securityConfig.getLogout();
        if (logout == null) {
            return false;
        }
        return Objects.equals(logout.getLogoutPath(), path);
    }

    /**
     * 当前请求是否是注册请求
     */
    public static boolean isRegisterRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isRegisterRequest(path, securityConfig);
    }

    public static boolean isRegisterRequest(String path, SecurityConfig securityConfig) {
        UserRegisterConfig register = securityConfig.getRegister();
        if (register == null) {
            return false;
        }
        return Objects.equals(register.getRegisterPath(), path);
    }

    /**
     * 当前请求是否是注册验证码(登录名注册-验证码)
     */
    public static boolean isLoginNameRegisterCaptchaRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isLoginNameRegisterCaptchaRequest(path, securityConfig);
    }

    public static boolean isLoginNameRegisterCaptchaRequest(String path, SecurityConfig securityConfig) {
        UserRegisterConfig register = securityConfig.getRegister();
        if (register == null) {
            return false;
        }
        LoginNameRegisterConfig loginNameRegister = register.getLoginNameRegister();
        if (loginNameRegister == null) {
            return false;
        }
        return Objects.equals(loginNameRegister.getRegisterCaptchaPath(), path);
    }

    /**
     * 当前请求是否是注册验证码(短信注册-图片验证码)
     */
    public static boolean isSmsRegisterCaptchaRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isSmsRegisterCaptchaRequest(path, securityConfig);
    }

    public static boolean isSmsRegisterCaptchaRequest(String path, SecurityConfig securityConfig) {
        UserRegisterConfig register = securityConfig.getRegister();
        if (register == null) {
            return false;
        }
        SmsRegisterConfig smsRegister = register.getSmsRegister();
        if (smsRegister == null) {
            return false;
        }
        return Objects.equals(smsRegister.getRegisterCaptchaPath(), path);
    }

    /**
     * 当前请求是否是注册验证码(短信注册-短信验证码)
     */
    public static boolean isSmsRegisterSmsValidateCodeRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isSmsRegisterSmsValidateCodeRequest(path, securityConfig);
    }

    public static boolean isSmsRegisterSmsValidateCodeRequest(String path, SecurityConfig securityConfig) {
        UserRegisterConfig register = securityConfig.getRegister();
        if (register == null) {
            return false;
        }
        SmsRegisterConfig smsRegister = register.getSmsRegister();
        if (smsRegister == null) {
            return false;
        }
        return Objects.equals(smsRegister.getRegisterSmsValidateCodePath(), path);
    }

    /**
     * 当前请求是否是注册验证码(邮箱注册-图片验证码)
     */
    public static boolean isEmailRegisterCaptchaRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isEmailRegisterCaptchaRequest(path, securityConfig);
    }

    public static boolean isEmailRegisterCaptchaRequest(String path, SecurityConfig securityConfig) {
        UserRegisterConfig register = securityConfig.getRegister();
        if (register == null) {
            return false;
        }
        EmailRegisterConfig emailRegister = register.getEmailRegister();
        if (emailRegister == null) {
            return false;
        }
        return Objects.equals(emailRegister.getRegisterCaptchaPath(), path);
    }

    /**
     * 当前请求是否是注册验证码(邮箱注册-邮箱验证码)
     */
    public static boolean isEmailRegisterEmailValidateCodeRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isEmailRegisterEmailValidateCodeRequest(path, securityConfig);
    }

    public static boolean isEmailRegisterEmailValidateCodeRequest(String path, SecurityConfig securityConfig) {
        UserRegisterConfig register = securityConfig.getRegister();
        if (register == null) {
            return false;
        }
        EmailRegisterConfig emailRegister = register.getEmailRegister();
        if (emailRegister == null) {
            return false;
        }
        return Objects.equals(emailRegister.getRegisterEmailValidateCodePath(), path);
    }

    /**
     * 当前请求是否需要身份认证
     */
    public static boolean isAuthenticationRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        final String path = getPath(request);
        return isAuthenticationRequest(path, request.getMethod(), securityConfig);
    }

    public static boolean isAuthenticationRequest(String path, String method, SecurityConfig securityConfig) {
        // 当前请求是“登录请求”或“验证码请求”或“注册请求”
        if (isLoginRequest(path, method, securityConfig)
                || isLoginCaptchaPath(path, securityConfig)
                || isLoginSmsValidateCodePath(path, securityConfig)
                || isLoginEmailValidateCodePath(path, securityConfig)
                || isGetScanCodeLoginPath(path, securityConfig)
                || isScanCodeStatePath(path, securityConfig)
                || isRegisterRequest(path, securityConfig)
                || isLoginNameRegisterCaptchaRequest(path, securityConfig)
                || isSmsRegisterCaptchaRequest(path, securityConfig)
                || isSmsRegisterSmsValidateCodeRequest(path, securityConfig)
                || isEmailRegisterCaptchaRequest(path, securityConfig)
                || isEmailRegisterEmailValidateCodeRequest(path, securityConfig)) {
            return false;
        }
        List<String> ignorePaths = securityConfig.getIgnorePaths();
        if (ignorePaths == null || ignorePaths.isEmpty()) {
            return true;
        }
        for (String ignorePath : ignorePaths) {
            if (Ant_Path_Matcher.match(ignorePath, path)) {
                // 忽略当前路径
                return false;
            }
        }
        return true;
    }

    /**
     * 当前请求是否需要授权
     */
    public static boolean isAuthorizationRequest(HttpServletRequest request, SecurityConfig securityConfig) {
        // 不需要授权的Path
        final String path = getPath(request);
        return isAuthorizationRequest(path, request.getMethod(), securityConfig);
    }

    /**
     * 当前请求是否需要授权
     */
    public static boolean isAuthorizationRequest(String path, String method, SecurityConfig securityConfig) {
        // 当前请求不需要身份认证 - 那就更不需要授权了
        if (!isAuthenticationRequest(path, method, securityConfig)) {
            return false;
        }
        List<String> ignoreAuthPaths = securityConfig.getIgnoreAuthPaths();
        if (ignoreAuthPaths == null || ignoreAuthPaths.isEmpty()) {
            return true;
        }
        for (String ignorePath : ignoreAuthPaths) {
            if (Ant_Path_Matcher.match(ignorePath, path)) {
                // 忽略当前路径
                return false;
            }
        }
        return true;
    }
}
