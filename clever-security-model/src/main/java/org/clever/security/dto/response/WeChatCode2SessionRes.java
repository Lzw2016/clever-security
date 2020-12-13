package org.clever.security.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:02 <br/>
 */
@Data
public class WeChatCode2SessionRes implements Serializable {
    /**
     * 用户唯一标识
     */
    @JsonProperty("openid")
    private String openId;
    /**
     * 会话密钥
     */
    @JsonProperty("session_key")
    private String sessionKey;
    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
     */
    @JsonProperty("unionid")
    private String unionId;
    /**
     * 错误码
     * <pre>
     *   -1         系统繁忙，此时请开发者稍候再试
     *   0          请求成功
     *   40029      code 无效
     *   45011      频率限制，每个用户每分钟100次
     * </pre>
     */
    @JsonProperty("errcode")
    private Integer errCode;
    /**
     * 错误信息
     */
    @JsonProperty("errmsg")
    private String errMsg;
}
