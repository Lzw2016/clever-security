package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.dto.request.admin.ServerAccessTokenAddReq;
import org.clever.security.dto.request.admin.ServerAccessTokenQueryReq;
import org.clever.security.dto.request.admin.ServerAccessTokenUpdateReq;
import org.clever.security.dto.response.admin.ServerAccessTokenQueryRes;
import org.clever.security.entity.Domain;
import org.clever.security.entity.ServerAccessToken;
import org.clever.security.mapper.DomainMapper;
import org.clever.security.mapper.ServerAccessTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 11:36 <br/>
 */
@Transactional(readOnly = true)
@Service
public class ServerAccessTokenService {
    @Autowired
    private ServerAccessTokenMapper serverAccessTokenMapper;
    @Autowired
    private DomainMapper domainMapper;

    public IPage<ServerAccessTokenQueryRes> pageQuery(ServerAccessTokenQueryReq req) {
        req.addOrderFieldMapping("id", "id");
        req.addOrderFieldMapping("domainId", "domain_id");
        req.addOrderFieldMapping("tag", "tag");
        req.addOrderFieldMapping("tokenName", "token_name");
        req.addOrderFieldMapping("tokenValue", "token_value");
        req.addOrderFieldMapping("expiredTime", "expired_time");
        req.addOrderFieldMapping("disable", "disable");
        req.addOrderFieldMapping("description", "description");
        req.addOrderFieldMapping("createAt", "create_at");
        req.addOrderFieldMapping("updateAt", "update_at");
        if (req.getOrderFields().isEmpty()) {
            req.addOrderField("createAt", DomainQueryReq.DESC);
        }
        return req.result(serverAccessTokenMapper.pageQuery(req));
    }

    @Transactional
    public ServerAccessToken addServerAccessToken(ServerAccessTokenAddReq req) {
        ServerAccessToken serverAccessToken = BeanMapper.mapper(req, ServerAccessToken.class);
        serverAccessToken.setId(SnowFlake.SNOW_FLAKE.nextId());
        validatedServerAccessToken(serverAccessToken);
        serverAccessTokenMapper.insert(serverAccessToken);
        return serverAccessTokenMapper.selectById(serverAccessToken.getId());
    }

    @Transactional
    public ServerAccessToken updateServerAccessToken(ServerAccessTokenUpdateReq req) {
        ServerAccessToken serverAccessToken = BeanMapper.mapper(req, ServerAccessToken.class);
        int exists = serverAccessTokenMapper.updateById(serverAccessToken);
        if (exists <= 0) {
            throw new BusinessException("ServerAccessToken不存在");
        }
        return serverAccessTokenMapper.selectById(serverAccessToken.getId());
    }

    @Transactional
    public ServerAccessToken delServerAccessToken(Long id) {
        ServerAccessToken serverAccessToken = serverAccessTokenMapper.selectById(id);
        int exists = serverAccessTokenMapper.deleteById(serverAccessToken.getId());
        if (exists <= 0) {
            throw new BusinessException("ServerAccessToken不存在");
        }
        return serverAccessToken;
    }

    protected void validatedServerAccessToken(ServerAccessToken serverAccessToken) {
        Domain domain = domainMapper.selectById(serverAccessToken.getDomainId());
        if (domain == null) {
            throw new BusinessException("数据域不存在");
        }
    }
}
