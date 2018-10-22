package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.security.entity.ServiceSys;
import org.clever.security.mapper.ServiceSysMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-22 20:36 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class ServiceSysService {

    @Autowired
    private ServiceSysMapper serviceSysMapper;

    public List<ServiceSys> selectAll() {
        return serviceSysMapper.selectList(null);
    }

    @Transactional
    public ServiceSys registerSys(ServiceSys serviceSys) {
        ServiceSys existsSys = serviceSysMapper.getBySysName(serviceSys.getSysName());
        if (existsSys != null) {
            if (!Objects.equals(existsSys.getRedisNameSpace(), serviceSys.getRedisNameSpace())) {
                throw new BusinessException("全局的Session Redis前缀配置错误，正确配置：" + existsSys.getRedisNameSpace());
            }
            return existsSys;
        }
        // 注册系统
        serviceSysMapper.insert(serviceSys);
        return serviceSysMapper.selectById(serviceSys.getId());
    }

    @Transactional
    public ServiceSys delServiceSys(String sysName) {
        ServiceSys existsSys = serviceSysMapper.getBySysName(sysName);
        if (existsSys == null) {
            throw new BusinessException("系统不存在：" + sysName);
        }
        serviceSysMapper.deleteById(existsSys.getId());
        return existsSys;
    }
}
