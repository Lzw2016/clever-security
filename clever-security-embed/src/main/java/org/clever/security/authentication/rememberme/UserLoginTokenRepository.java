package org.clever.security.authentication.rememberme;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.RememberMeTokenClient;
import org.clever.security.dto.request.RememberMeTokenAddReq;
import org.clever.security.dto.request.RememberMeTokenUpdateReq;
import org.clever.security.entity.RememberMeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 存储“记住我”功能产生的Token
 * 作者： lzw<br/>
 * 创建时间：2018-09-21 19:52 <br/>
 */
@Transactional(readOnly = true)
@Component
@Slf4j
public class UserLoginTokenRepository implements PersistentTokenRepository {

    @Autowired
    private RememberMeTokenClient rememberMeTokenClient;

    @Transactional
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        RememberMeTokenAddReq req = new RememberMeTokenAddReq();
        req.setSeries(token.getSeries());
        req.setUsername(token.getUsername());
        req.setToken(token.getTokenValue());
        req.setLastUsed(token.getDate());
        rememberMeTokenClient.addRememberMeToken(req);
    }

    @Transactional
    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        RememberMeTokenUpdateReq req = new RememberMeTokenUpdateReq();
        req.setToken(tokenValue);
        req.setLastUsed(lastUsed);
        rememberMeTokenClient.updateRememberMeToken(series, req);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        RememberMeToken rememberMeToken = rememberMeTokenClient.getRememberMeToken(seriesId);
        if (rememberMeToken == null) {
            return null;
        }
        return new PersistentRememberMeToken(
                rememberMeToken.getUsername(),
                rememberMeToken.getSeries(),
                rememberMeToken.getToken(),
                rememberMeToken.getLastUsed()
        );
    }

    @Transactional
    @Override
    public void removeUserTokens(String username) {
        rememberMeTokenClient.delRememberMeToken(username);
    }
}
