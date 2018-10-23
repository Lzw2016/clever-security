package org.clever.security.client;

import org.clever.security.dto.request.ServiceSysAddReq;
import org.clever.security.entity.ServiceSys;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-23 10:23 <br/>
 */
@FeignClient(name = "clever-security-server", path = "/api")
public interface ServiceSysClient {

    /**
     * 查询所有系统信息
     */
    @GetMapping("/service_sys")
    List<ServiceSys> allSysName();

    /**
     * 注册系统
     */
    @PostMapping("/service_sys")
    ServiceSys registerSys(@RequestBody ServiceSysAddReq serviceSysAddReq);

    /**
     * 删除系统
     */
    @DeleteMapping("/service_sys/{sysName}")
    ServiceSys delServiceSys(@PathVariable("sysName") String sysName);
}
