package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.admin.DomainAddReq;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.dto.request.admin.DomainUpdateReq;
import org.clever.security.entity.Domain;
import org.clever.security.mapper.DomainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class DomainService {
    @Autowired
    private DomainMapper domainMapper;

    public List<Domain> all() {
        return domainMapper.selectList(Wrappers.emptyWrapper());
    }

    public IPage<Domain> pageQuery(DomainQueryReq req) {
        req.addOrderFieldMapping("id", "id");
        req.addOrderFieldMapping("name", "name");
        req.addOrderFieldMapping("redisNameSpace", "redis_name_space");
        req.addOrderFieldMapping("createAt", "create_at");
        req.addOrderFieldMapping("updateAt", "update_at");
        if (req.getOrderFields().isEmpty()) {
            req.addOrderField("createAt", DomainQueryReq.DESC);
        }
        return req.result(domainMapper.pageQuery(req));
    }

    @Transactional
    public Domain addDomain(DomainAddReq req) {
        Domain domain = BeanMapper.mapper(req, Domain.class);
        domain.setId(SnowFlake.SNOW_FLAKE.nextId());
        validatedDomain(domain);
        domainMapper.insert(domain);
        return domainMapper.selectById(domain.getId());
    }

    @Transactional
    public Domain updateDomain(DomainUpdateReq req) {
        Domain domain = BeanMapper.mapper(req, Domain.class);
        validatedDomain(domain);
        int exists = domainMapper.updateById(domain);
        if (exists <= 0) {
            throw new BusinessException("数据域不存在");
        }
        return domainMapper.selectById(domain.getId());
    }

    protected void validatedDomain(Domain domain) {
        Domain exists = domainMapper.getByName(domain.getName());
        if (exists != null && !Objects.equals(domain.getId(), exists.getId())) {
            throw new BusinessException("域名称已经存在");
        }
        exists = domainMapper.getByRedisNameSpace(domain.getRedisNameSpace());
        if (exists != null && !Objects.equals(domain.getId(), exists.getId())) {
            throw new BusinessException("Redis前缀已经存在");
        }
    }
}
