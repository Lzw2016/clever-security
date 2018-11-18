//package org.clever.security.jwt.controller;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.clever.security.dto.response.UserRes;
//import org.clever.security.utils.AuthenticationUtils;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 作者： lzw<br/>
// * 创建时间：2018-11-12 17:03 <br/>
// */
//@Api(description = "当前登录用户信息")
//@RestController
//@Slf4j
//public class CurrentLoginUserController {
//
//    @ApiOperation("获取当前登录用户信息")
//    @GetMapping("/login/user_info.json")
//    public UserRes currentLoginUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return AuthenticationUtils.getUserRes(authentication);
//    }
//}
