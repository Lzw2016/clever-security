package org.clever.security.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户信息(从数据库或其它服务加载)
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 19:27 <br/>
 */
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
    private Map<String, Object> extInfo;

//    /**
//     * 用户昵称
//     */
//    private String nickname;
//    /**
//     * 用户头像
//     */
//    private String avatar;
//    /**
//     * 用户注册渠道，0:管理员，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
//     */
//    private Integer registerChannel;
//    /**
//     * 用户来源，0:系统注册，1:外部导入(同步)
//     */
//    private Integer fromSource;
//    /**
//     * 说明
//     */
//    private String description;
//    /**
//     * 创建时间
//     */
//    private Date createAt;
//    /**
//     * 更新时间
//     */
//    private Date updateAt;
}
