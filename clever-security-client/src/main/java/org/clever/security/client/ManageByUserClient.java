package org.clever.security.client;

import org.clever.security.dto.request.UserAddReq;
import org.clever.security.dto.response.UserAddRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "clever-security-server", path = "/api/manage")
public interface ManageByUserClient {

    /**
     * 新增用户
     */
    @PostMapping("/user")
    UserAddRes addUser(@RequestBody UserAddReq userAddReq);
}
