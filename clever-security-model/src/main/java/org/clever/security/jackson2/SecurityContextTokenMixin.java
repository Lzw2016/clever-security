package org.clever.security.jackson2;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-22 21:14 <br/>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
class SecurityContextTokenMixin {

    @JsonCreator
    public SecurityContextTokenMixin(@JsonProperty("userDetails") UserDetails userDetails) {
    }
}
