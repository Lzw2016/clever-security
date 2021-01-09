package org.clever.security.embed.event;

import io.jsonwebtoken.Claims;
import lombok.Data;
import org.clever.security.model.SecurityContext;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/05 18:45 <br/>
 */
@Data
public class AuthenticationSuccessEvent implements Serializable {
    /**
     * JWT-Token
     */
    private final String jwtToken;
    /**
     * 用户id
     */
    private final String uid;
    /**
     * JWT-Token body 信息
     */
    private final Claims claims;
    /**
     * 安全上下文(用户信息)
     */
    private final SecurityContext securityContext;

    public AuthenticationSuccessEvent(String jwtToken, String uid, Claims claims, SecurityContext securityContext) {
        this.jwtToken = jwtToken;
        this.uid = uid;
        this.claims = claims;
        this.securityContext = securityContext;
    }
}
