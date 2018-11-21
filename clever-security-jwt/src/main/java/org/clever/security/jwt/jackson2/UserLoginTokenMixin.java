package org.clever.security.jwt.jackson2;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-22 21:14 <br/>
 */
@SuppressWarnings("unused")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
class UserLoginTokenMixin {

    @JsonCreator
    public UserLoginTokenMixin(@JsonProperty("userDetails") UserDetails userDetails) {
    }
}
