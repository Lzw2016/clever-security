package org.clever.security.entity.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 16:56 <br/>
 */
@Data
public class WebPermissionModel implements Serializable, Comparable<WebPermissionModel> {
    private Long permissionId;

    /**
     * 系统(或服务)名称
     */
    private String sysName;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源访问所需要的权限标识字符串
     */
    private String permissionStr;

    /**
     * 资源类型（1:请求URL地址, 2:其他资源）
     */
    private Integer resourcesType;

    /**
     * 资源说明
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    // ----------------------------------------------------------------

    private Long webPermissionId;

    /**
     * 需要授权才允许访问（1：需要；2：不需要）
     */
    private Integer needAuthorization;

    /**
     * Spring Controller类名称
     */
    private String targetClass;

    /**
     * Spring Controller类的方法名称
     */
    private String targetMethod;

    /**
     * Spring Controller类的方法参数签名
     */
    private String targetMethodParams;

    /**
     * 资源URL地址
     */
    private String resourcesUrl;

    /**
     * Spring Controller路由资源是否存在，0：不存在；1：存在
     */
    private Integer targetExist;

    /**
     * 定义排序规则
     */
    @Override
    public int compareTo(WebPermissionModel permission) {
        // module resourcesType targetClass targetMethod targetMethodParams title
        String format = "%1$-128s|%2$-20s|%3$-255s|%4$-255s|%5$-255s|%6$-255s";
        String strA = String.format(format,
                this.getSysName(),
                this.getResourcesType(),
                this.getTargetClass(),
                this.getTargetMethod(),
                this.getTargetMethodParams(),
                this.getTitle());
        String strB = String.format(format,
                permission.getSysName(),
                permission.getResourcesType(),
                permission.getTargetClass(),
                permission.getTargetMethod(),
                permission.getTargetMethodParams(),
                permission.getTitle());
        return strA.compareTo(strB);
    }
}
