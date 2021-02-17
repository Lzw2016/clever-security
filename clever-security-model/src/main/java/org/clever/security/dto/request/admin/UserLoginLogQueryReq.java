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
public class UserLoginLogQueryReq extends QueryByPage {
    /**
     * 主键
     */
    private Long id;
    /**
     * 域id
     */
    private Long domainId;
    /**
     * 关键字搜索
     */
   private String loginIp;
    /**
     * 登录渠道
     */
   private Integer loginChannel;
    /**
     * 登录方式
     */
   private Integer loginType;
    /**
     * 登录状态
     */
   private Integer loginState;
    /**
     * 登录时间 - 开始
     */
    private Date loginTimeStart;

    /**
     * 登录时间 - 结束
     */
    private Date loginTimeEnd;
}