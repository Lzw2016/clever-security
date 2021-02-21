package org.clever.security.service.admin;

import org.clever.common.exception.BusinessException;
import org.clever.security.mapper.UserDomainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class UserDomainService {
    @Autowired
    private UserDomainMapper userDomainMapper;

    @Transactional
    public Boolean delUserDomain(Long domainId, String uid) {
        int exists = userDomainMapper.exists(domainId, uid);
        if (exists <= 0) {
            throw new BusinessException("用户不存在");
        }
        userDomainMapper.deleteByDomainId(domainId, uid);
        return true;
    }
}
