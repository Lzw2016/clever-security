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
public class MenuPermissionQueryReq extends QueryByPage {
    /**
     * id
     */
    private Long id;
    /**
     * 域id
     */
    private Long domainId;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单路径
     */
    private String path;
    /**
     * 页面路径
     */
    private String pagePath;
    /**
     * 隐藏当前菜单和子菜单，0:不隐藏(显示)，1:隐藏
     */
    private Integer hideMenu;
    /**
     * 隐藏子菜单，0:不隐藏(显示)，1:隐藏
     */
    private Integer hideChildrenMenu;
    /**
     * 权限唯一字符串标识
     */
    private String strFlag;
    /**
     * 是否启用授权，0:不启用，1:启用
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