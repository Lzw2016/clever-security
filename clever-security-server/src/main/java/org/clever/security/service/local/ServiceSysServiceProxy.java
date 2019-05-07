package org.clever.security.service.local;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.client.ServiceSysClient;
import org.clever.security.dto.request.ServiceSysAddReq;
import org.clever.security.entity.ServiceSys;
import org.clever.security.service.ServiceSysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 参考 ServiceSysController
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-11-11 19:28 <br/>
 */
@Component
@Slf4j
public class ServiceSysServiceProxy implements ServiceSysClient {
    @Autowired
    private ServiceSysService serviceSysService;

    @Override
    public List<ServiceSys> allSysName() {
        return serviceSysService.selectAll();
    }

    @Override
    public ServiceSys registerSys(ServiceSysAddReq serviceSysAddReq) {
        ServiceSys serviceSys = BeanMapper.mapper(serviceSysAddReq, ServiceSys.class);
        return serviceSysService.registerSys(serviceSys);
    }

    @Override
    public ServiceSys delServiceSys(String sysName) {
        return serviceSysService.delServiceSys(sysName);
    }
}
