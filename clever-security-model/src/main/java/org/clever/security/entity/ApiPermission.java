package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * API权限表(permission子表)(ApiPermission)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:38
 */
@Data
public class ApiPermission implements Serializable {
    private static final long serialVersionUID = 246991777557100991L;
    /**
     * api id(系统自动生成且不会变化)
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 权限id
     */
    private Long permissionId;

    /**
     * API标题
     */
    private String title;

    /**
     * controller类名称
     */
    private String className;

    /**
     * controller类的方法名称
     */
    private String methodName;

    /**
     * controller类的方法参数签名
     */
    private String methodParams;

    /**
     * API接口地址(只用作显示使用)
     */
    private String apiPath;

    /**
     * API接口是否存在，0：不存在；1：存在
     */
    private Integer apiExist;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}