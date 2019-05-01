package org.clever.security.jackson2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-22 21:28 <br/>
 */
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
class LoginUserDetailsMixin {

    @JsonCreator
    public LoginUserDetailsMixin(
            @JsonProperty("id") Long id,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("userType") Integer userType,
            @JsonProperty("telephone") String telephone,
            @JsonProperty("email") String email,
            @JsonProperty("expiredTime") Date expiredTime,
            @JsonProperty("accountNonLocked") boolean locked,
            @JsonProperty("enabled") boolean enabled,
            @JsonProperty("description") String description,
            @JsonProperty("createAt") Date createAt,
            @JsonProperty("updateAt") Date updateAt) {
    }
}
