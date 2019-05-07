package org.clever.security.jackson2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-22 22:02 <br/>
 */
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
class UserAuthorityMixin {

    @JsonCreator
    public UserAuthorityMixin(@JsonProperty("authority") String authority, @JsonProperty("title") String title) {
    }
}
