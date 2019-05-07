package org.clever.security.service.local;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.client.RememberMeTokenClient;
import org.clever.security.dto.request.RememberMeTokenAddReq;
import org.clever.security.dto.request.RememberMeTokenUpdateReq;
import org.clever.security.entity.RememberMeToken;
import org.clever.security.service.RememberMeTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 参考 RememberMeTokenController
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-11-11 19:21 <br/>
 */
@Component
@Slf4j
public class RememberMeTokenServiceProxy implements RememberMeTokenClient {

    @Autowired
    private RememberMeTokenService rememberMeTokenService;

    @Override
    public RememberMeToken addRememberMeToken(RememberMeTokenAddReq req) {
        RememberMeToken rememberMeToken = BeanMapper.mapper(req, RememberMeToken.class);
        return rememberMeTokenService.addRememberMeToken(rememberMeToken);
    }

    @Override
    public RememberMeToken getRememberMeToken(String series) {
        return rememberMeTokenService.getRememberMeToken(series);
    }

    @Override
    public Map<String, Object> delRememberMeToken(String username) {
        Map<String, Object> map = new HashMap<>();
        Integer delCount = rememberMeTokenService.delRememberMeToken(username);
        map.put("delCount", delCount);
        return map;
    }

    @Override
    public RememberMeToken updateRememberMeToken(String series, RememberMeTokenUpdateReq req) {
        return rememberMeTokenService.updateRememberMeToken(series, req.getToken(), req.getLastUsed());
    }
}
