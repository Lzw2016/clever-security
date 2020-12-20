package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登录失败计数表(缓存表)(LoginFailedCount)实体类
 *
 * @author lizw
 * @since 2020-12-13 14:33:32
 */
@Data
public class LoginFailedCount implements Serializable {
    private static final long serialVersionUID = 376326058097032378L;
    /**
     * id(系统自动生成且不会变化)
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 登录用户id
     */
    private String uid;

    /**
     * 登录方式，1:用户名密码，2:手机号验证码，3:邮箱验证码，4:刷新令牌，5:微信小程序，6:扫码登录
     */
    private Integer loginType;

    /**
     * 登录失败次数
     */
    private Integer failedCount;

    /**
     * 最后登录失败时间
     */
    private Date lastLoginTime;

    /**
     * 数据删除标志，0:未删除，1:已删除
     */
    private Integer deleteFlag;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

}