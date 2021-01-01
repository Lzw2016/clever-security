package org.clever.security.jackson2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.clever.security.model.UserInfo;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/19 13:47 <br/>
 */
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
class SecurityContextMixin {

    @JsonCreator
    public SecurityContextMixin(@JsonProperty("userInfo") UserInfo userInfo) {
    }
}
