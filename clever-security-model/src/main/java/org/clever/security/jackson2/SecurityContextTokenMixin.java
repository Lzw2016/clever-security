package org.clever.security.jackson2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.clever.security.model.LoginUserDetails;
import org.clever.security.token.login.BaseLoginToken;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-22 21:14 <br/>
 */
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
class SecurityContextTokenMixin {

    @JsonCreator
    public SecurityContextTokenMixin(@JsonProperty("credentials") BaseLoginToken loginToken, @JsonProperty("principal") LoginUserDetails userDetails) {
    }
}
