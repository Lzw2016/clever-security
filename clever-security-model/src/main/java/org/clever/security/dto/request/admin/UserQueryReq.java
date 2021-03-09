package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryReq extends QueryByPage {
    /**
     * 域id
     */
    private Long domainId;

    /**
     * uid、login_name、telephone、email、nickname
     */
    private String userSearchKey;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 用户注册渠道
     */
    private Integer registerChannel;

    /**
     * 用户来源
     */
    private Integer fromSource;

    /**
     * 帐号过期时间 - 开始
     */
    private Date expiredTimeStart;

    /**
     * 帐号过期时间 - 结束
     */
    private Date expiredTimeEnd;

    /**
     * 创建时间 - 开始
     */
    private Date createAtStart;

    /**
     * 创建时间 - 结束
     */
    private Date createAtEnd;
}