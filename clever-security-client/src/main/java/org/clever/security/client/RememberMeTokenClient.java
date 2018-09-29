package org.clever.security.client;

import org.clever.security.dto.request.RememberMeTokenAddReq;
import org.clever.security.dto.request.RememberMeTokenUpdateReq;
import org.clever.security.entity.RememberMeToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 18:45 <br/>
 */
@FeignClient(name = "clever-security-server", path = "/api", url = "http://localhost:28080")
public interface RememberMeTokenClient {

    /**
     * 新增RememberMeToken
     */
    @PostMapping("/remember_me_token")
    RememberMeToken addRememberMeToken(@RequestBody RememberMeTokenAddReq req);

    /**
     * 读取RememberMeToken
     */
    @GetMapping("/remember_me_token/{series}")
    RememberMeToken getRememberMeToken(@PathVariable("series") String series);

    /**
     * 删除RememberMeToken
     */
    @DeleteMapping("/remember_me_token/{username}")
    Map<String, Object> delRememberMeToken(@PathVariable("username") String username);

    /**
     * 修改RememberMeToken
     */
    @PutMapping("/remember_me_token/{series}")
    RememberMeToken updateRememberMeToken(@PathVariable("series") String series, @RequestBody RememberMeTokenUpdateReq req);
}