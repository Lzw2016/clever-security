package org.clever.security.demo.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-14 18:05 <br/>
 */
@Api(description = "测试")
@RequestMapping("/test")
@RestController
@Slf4j
public class TestController {

    @GetMapping("/01")
    public Object test() {
        return "test";
    }

    @GetMapping("/02")
    public Object test2() {
        throw new BusinessException("异常");
    }
}
