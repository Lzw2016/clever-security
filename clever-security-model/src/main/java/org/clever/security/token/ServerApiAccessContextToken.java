package org.clever.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.ArrayList;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-05-16 13:55 <br/>
 */
public class ServerApiAccessContextToken extends AbstractAuthenticationToken {

    /**
     * 请求头Token名称
     */
    private String tokenName;

    /**
     * 请求头Token值
     */
    private String tokenValue;

    /**
     * @param tokenName  请求头Token名称
     * @param tokenValue 请求头Token值
     */
    public ServerApiAccessContextToken(String tokenName, String tokenValue) {
        super(new ArrayList<>());
        this.tokenName = tokenName;
        this.tokenValue = tokenValue;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return tokenValue;
    }

    @Override
    public Object getPrincipal() {
        return tokenName;
    }

    @Override
    public String toString() {
        return "ServerApiAccessContextToken{" +
                "tokenName='" + tokenName + '\'' +
                ", tokenValue='" + tokenValue + '\'' +
                '}';
    }
}
