package org.clever.security.admin.controller;

import org.clever.common.model.response.AjaxMessage;
import org.clever.security.annotation.UrlAuthorization;
import org.clever.security.entity.Domain;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/29 21:52 <br/>
 */
@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    @GetMapping("/t01")
    public Object t01() {
        return new AjaxMessage<>("t01", "成功");
    }

    @PostMapping("/t01")
    public Object t0t(@RequestBody Domain domain) {
        return new AjaxMessage<>(domain, "成功");
    }

    @UrlAuthorization(title = "测试02", description = "测试接口", strFlag = "ProtectedController_t02_#1")
    @GetMapping("/t02/{param}")
    public Object t02(@PathVariable("param") String param) {
        return new AjaxMessage<>(param, "成功");
    }
}
