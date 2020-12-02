package org.clever.security.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息(从数据库或其它服务加载)
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 19:27 <br/>
 */
@Data
public class UserInfo implements Serializable {
    /**
     * 用户id(系统自动生成且不会变化)
     */
    private String uid;
    /**
     * 用户登录名(允许修改)
     */
    private String loginName;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String telephone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 帐号过期时间(空表示永不过期)
     */
    private Date expiredTime;
    /**
     * 是否启用，0:禁用，1:启用
     */
    private Integer enabled;
    /**
     * 用户扩展信息
     */
    private Map<String, Object> extInfo = new HashMap<>();
}
