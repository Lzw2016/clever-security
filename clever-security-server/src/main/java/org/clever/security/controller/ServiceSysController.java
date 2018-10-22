package org.clever.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.ServiceSysAddReq;
import org.clever.security.entity.ServiceSys;
import org.clever.security.service.ServiceSysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-22 20:34 <br/>
 */
@Api(description = "服务系统")
@RestController
@RequestMapping("/api")
public class ServiceSysController {

    @Autowired
    private ServiceSysService serviceSysService;

    @ApiOperation("查询所有系统信息")
    @GetMapping("/service_sys")
    public List<ServiceSys> allSysName() {
        return serviceSysService.selectAll();
    }

    @ApiOperation("注册系统")
    @PostMapping("/service_sys")
    public ServiceSys registerSys(@RequestBody @Validated ServiceSysAddReq serviceSysAddReq) {
        ServiceSys serviceSys = BeanMapper.mapper(serviceSysAddReq, ServiceSys.class);
        return serviceSysService.registerSys(serviceSys);
    }

    @ApiOperation("删除系统")
    @DeleteMapping("/service_sys/{sysName}")
    public ServiceSys delServiceSys(@PathVariable("sysName") String sysName) {
        return serviceSysService.delServiceSys(sysName);
    }
}
