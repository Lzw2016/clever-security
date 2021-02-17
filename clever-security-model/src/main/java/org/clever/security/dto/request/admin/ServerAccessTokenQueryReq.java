package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 11:39 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServerAccessTokenQueryReq extends QueryByPage {
    /**
     * 域id
     */
    private Long domainId;

    /**
     * Token标签
     */
    private String tag;

    /**
     * Token名称
     */
    private String tokenName;

    /**
     * Token值
     */
    private String tokenValue;

    /**
     * Token过期时间(空表示永不过期) - 开始
     */
    private Date expiredTimeStart;

    /**
     * Token过期时间(空表示永不过期) - 结束
     */
    private Date expiredTimeEnd;

    /**
     * Token是否禁用，0:未禁用；1:已禁用
     */
    private Integer disable;

    /**
     * 创建时间 - 开始
     */
    private Date createAtStart;

    /**
     * 创建时间 - 结束
     */
    private Date createAtEnd;
}
