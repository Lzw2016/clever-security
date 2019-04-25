package org.clever.security.session.jackson2;

import com.fasterxml.jackson.annotation.*;
import org.clever.security.entity.User;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-22 21:28 <br/>
 */
@SuppressWarnings("unused")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
class LoginUserDetailsMixin {

    @JsonCreator
    public LoginUserDetailsMixin(@JsonProperty("user") User user) {
    }
}
