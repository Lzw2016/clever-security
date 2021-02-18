package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.dto.request.admin.UserSecurityContextQueryReq;
import org.clever.security.dto.response.admin.UserSecurityContextQueryRes;
import org.clever.security.mapper.UserSecurityContextMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/18 15:26 <br/>
 */
@Transactional(readOnly = true)
@Service
public class UserSecurityContextService {
    @Autowired
    private UserSecurityContextMapper userSecurityContextMapper;

    public IPage<UserSecurityContextQueryRes> pageQuery(UserSecurityContextQueryReq req) {
        req.addOrderFieldMapping("id", "a.id");
        req.addOrderFieldMapping("domainId", "a.domain_id");
        req.addOrderFieldMapping("uid", "a.uid");
        req.addOrderFieldMapping("createAt", "a.create_at");
        req.addOrderFieldMapping("updateAt", "a.update_at");
        req.addOrderFieldMapping("loginName", "c.login_name");
        req.addOrderFieldMapping("email", "c.email");
        req.addOrderFieldMapping("nickname", "c.nickname");
        req.addOrderFieldMapping("telephone", "c.telephone");
        if (req.getOrderFields().isEmpty()) {
            req.addOrderField("createAt", DomainQueryReq.DESC);
        }
        return req.result(userSecurityContextMapper.pageQuery(req));
    }
}
