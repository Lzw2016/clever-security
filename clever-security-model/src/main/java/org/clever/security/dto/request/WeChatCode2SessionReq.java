package org.clever.security.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:01 <br/>
 */
@Data
public class WeChatCode2SessionReq implements Serializable {
    /**
     * 小程序 appId
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * 小程序 appSecret
     */
    @JsonProperty("secret")
    private String secret;
    /**
     * 登录时获取的 code
     */
    @JsonProperty("js_code")
    private String jsCode;
    /**
     * 授权类型，此处只需填写 authorization_code
     */
    @JsonProperty("grant_type")
    private String grantType = "authorization_code";

    public WeChatCode2SessionReq() {
    }

    public WeChatCode2SessionReq(String appId, String secret, String jsCode) {
        this.appId = appId;
        this.secret = secret;
        this.jsCode = jsCode;
    }
}
