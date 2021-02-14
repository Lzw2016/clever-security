package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 19:14 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class GetApiPermissionRes extends BaseResponse {
    /**
     * 上级权限id
     */
    private Long parentId;
    /**
     * 域id
     */
    private Long domainId;
    /**
     * 权限唯一字符串标识
     */
    private String strFlag;
    /**
     * 权限标题
     */
    private String title;
    /**
     * 权限类型，1:API权限, 2:菜单权限，3:UI组件权限
     */
    private Integer resourcesType;
    /**
     * 是否启用授权，0:不启用，1:启用
     */
    private Integer enable;
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
}
