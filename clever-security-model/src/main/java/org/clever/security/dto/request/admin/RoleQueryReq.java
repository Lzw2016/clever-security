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
public class RoleQueryReq extends QueryByPage {
    /**
     * id
     */
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 用户账号关键字
     */
    private String name;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 创建时间 - 开始
     */
    private Date createAtStart;

    /**
     * 创建时间 - 结束
     */
    private Date createAtEnd;
}