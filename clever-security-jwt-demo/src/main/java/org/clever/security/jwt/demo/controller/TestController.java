package org.clever.security.jwt.demo.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-14 18:05 <br/>
 */
@Api("测试")
@RequestMapping("/test")
@RestController
@Slf4j
public class TestController {

    @GetMapping("/01")
    public Object test(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            return null;
        }
        return httpSession.getAttributeNames();
    }

    @GetMapping("/02")
    public Object test2() {
        return SecurityContextHolder.getContext();
    }
}
