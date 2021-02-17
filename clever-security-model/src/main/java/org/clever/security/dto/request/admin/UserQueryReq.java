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
     * 用户登录名
     */
    private String loginName;

    /**
     * 用户手机号
     */
    private String telephone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 创建时间 - 开始
     */
    private Date createAtStart;

    /**
     * 创建时间 - 结束
     */
    private Date createAtEnd;
}