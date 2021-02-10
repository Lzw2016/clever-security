package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 验证码(缓存表)(ValidateCode)实体类
 *
 * @author lizw
 * @since 2020-11-28 19:47:43
 */
@Data
public class ValidateCode implements Serializable {
    private static final long serialVersionUID = 757086153879042300L;
    /**
     * 验证码 id(系统自动生成且不会变化)
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 用户id(触发生成验证码的用户)
     */
    private String uid;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码签名
     */
    private String digest;

    /**
     * 验证码类型
     * <pre>
     * 1:登录验证码，
     * 2:找回密码验证码，
     * 3:重置密码(修改密码)验证码，
     * 4:登录名注册验证码，
     * 5:短信注册图片验证码，
     * 6:短信注册短信验证码，
     * 7:邮箱注册图片验证码，
     * 8:邮箱注册邮箱验证码
     * 9:短信换绑图片验证码，
     * 10:短信换绑短信验证码，
     * 11:邮箱换绑图片验证码，
     * 12:邮箱换绑邮箱验证码
     * </pre>
     */
    private Integer type;

    /**
     * 验证码发送渠道，0:不需要发送，1:短信，2:email
     */
    private Integer sendChannel;

    /**
     * 发送目标手机号或邮箱
     */
    private String sendTarget;

    /**
     * 验证码过期时间
     */
    private Date expiredTime;

    /**
     * 验证码验证时间(使用时间)
     */
    private Date validateTime;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}