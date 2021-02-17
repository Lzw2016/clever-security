package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.common.exception.BusinessException;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.dto.request.admin.JwtTokenQueryReq;
import org.clever.security.dto.response.admin.JwtTokenQueryRes;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.JwtToken;
import org.clever.security.mapper.JwtTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 15:31 <br/>
 */
@Transactional(readOnly = true)
@Service
public class JwtTokenService {
    @Autowired
    private JwtTokenMapper jwtTokenMapper;

    public IPage<JwtTokenQueryRes> pageQuery(JwtTokenQueryReq req) {
        req.addOrderFieldMapping("id", "a.id");
        req.addOrderFieldMapping("domainId", "a.domainId");
        req.addOrderFieldMapping("domainName", "b.name");
        req.addOrderFieldMapping("uid", "a.uid");
        req.addOrderFieldMapping("expiredTime", "a.expiredTime");
        req.addOrderFieldMapping("disable", "a.disable");
        req.addOrderFieldMapping("refreshTokenExpiredTime", "a.refreshTokenExpiredTime");
        req.addOrderFieldMapping("refreshTokenState", "a.refreshTokenState");
        req.addOrderFieldMapping("refreshTokenUseTime", "a.refreshTokenUseTime");
        req.addOrderFieldMapping("createAt", "a.create_at");
        req.addOrderFieldMapping("updateAt", "a.update_at");
        req.addOrderFieldMapping("loginName", "c.login_name");
        req.addOrderFieldMapping("telephone", "c.telephone");
        req.addOrderFieldMapping("email", "c.email");
        req.addOrderFieldMapping("nickname", "c.nickname");
        if (req.getOrderFields().isEmpty()) {
            req.addOrderField("createAt", DomainQueryReq.DESC);
        }
        return req.result(jwtTokenMapper.pageQuery(req));
    }

    @Transactional
    public JwtToken disableJwtToken(Long id) {
        JwtToken update = new JwtToken();
        update.setId(id);
        update.setDisable(EnumConstant.JwtToken_Disable_1);
        update.setDisableReason("管理员手动禁用");
        int exists = jwtTokenMapper.updateById(update);
        if (exists <= 0) {
            throw new BusinessException("JwtToken不存在");
        }
        return jwtTokenMapper.selectById(id);
    }
}
