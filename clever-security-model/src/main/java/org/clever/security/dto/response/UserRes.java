package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-03-20 16:13 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRes extends BaseResponse {

    /**
     * 登录名
     */
    private String username;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户类型，0：系统内建，1：外部系统用户
     */
    private Integer userType;

    /**
     * 用户有角色名
     */
    private List<String> roleNames = new ArrayList<>();

    /**
     * 拥有的权限字符串
     */
    private List<String> authorities = new ArrayList<>();
}
