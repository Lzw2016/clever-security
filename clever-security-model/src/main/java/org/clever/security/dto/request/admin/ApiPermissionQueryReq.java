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
public class ApiPermissionQueryReq extends QueryByPage {
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
   private String apiExist;
    /**
     * 创建时间 - 开始
     */
    private Date createAtStart;

    /**
     * 创建时间 - 结束
     */
    private Date createAtEnd;
}