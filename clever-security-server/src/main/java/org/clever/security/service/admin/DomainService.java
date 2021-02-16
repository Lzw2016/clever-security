package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.entity.Domain;
import org.clever.security.mapper.DomainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional
@Service
public class DomainService {
    @Autowired
    private DomainMapper domainMapper;

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
}
