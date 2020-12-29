package org.clever.security.admin.controller;

import org.clever.common.model.response.AjaxMessage;
import org.clever.security.entity.Domain;
import org.springframework.web.bind.annotation.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/29 21:07 <br/>
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/t01")
    public Object t01() {
        return new AjaxMessage<>("t01", "成功");
    }

    @PostMapping("/t01")
    public Object t0t(@RequestBody Domain domain) {
        return new AjaxMessage<>(domain, "成功");
    }

    @GetMapping("/t02/{param}")
    public Object t02(@PathVariable("param") String param) {
        return new AjaxMessage<>(param, "成功");
    }
}
