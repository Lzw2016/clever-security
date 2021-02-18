package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.dto.request.admin.ValidateCodeQueryReq;
import org.clever.security.dto.response.admin.ValidateCodeQueryRes;
import org.clever.security.mapper.ValidateCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/18 14:04 <br/>
 */
@Transactional(readOnly = true)
@Service
public class ValidateCodeService {
    @Autowired
    private ValidateCodeMapper validateCodeMapper;

    public IPage<ValidateCodeQueryRes> pageQuery(ValidateCodeQueryReq req) {
        req.addOrderFieldMapping("id", "a.id");
        req.addOrderFieldMapping("domainId", "a.domainId");
        req.addOrderFieldMapping("uid", "a.uid");
        req.addOrderFieldMapping("code", "a.code");
        req.addOrderFieldMapping("type", "a.type");
        req.addOrderFieldMapping("sendChannel", "a.send_channel");
        req.addOrderFieldMapping("sendTarget", "a.send_target");
        req.addOrderFieldMapping("expiredTime", "a.expired_time");
        req.addOrderFieldMapping("validateTime", "a.validate_time");
        req.addOrderFieldMapping("createAt", "a.create_at");
        req.addOrderFieldMapping("updateAt", "a.update_at");
        req.addOrderFieldMapping("loginName", "c.login_name");
        req.addOrderFieldMapping("email", "c.email");
        req.addOrderFieldMapping("telephone", "c.telephone");
        req.addOrderFieldMapping("nickname", "c.nickname");
        if (req.getOrderFields().isEmpty()) {
            req.addOrderField("createAt", DomainQueryReq.DESC);
        }
        return req.result(validateCodeMapper.pageQuery(req));
    }
}
