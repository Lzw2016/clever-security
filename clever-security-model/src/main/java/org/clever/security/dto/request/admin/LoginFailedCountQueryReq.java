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
public class LoginFailedCountQueryReq extends QueryByPage {
    /**
     * 主键
     */
    private Long id;
    /**
     * 域id
     */
    private Long domainId;
    /**
     * 登录方式
     */
    private Integer loginType;
    /**
     * 用户搜索关键字
     */
    private String userKeyword;
    /**
     * 创建时间 - 开始
     */
    private Date lastLoginTimeStart;

    /**
     * 创建时间 - 结束
     */
    private Date lastLoginTimeEnd;
}