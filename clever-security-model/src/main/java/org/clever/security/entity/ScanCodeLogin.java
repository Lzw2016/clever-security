package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 扫码登录表(缓存表)(ScanCodeLogin)实体类
 *
 * @author lizw
 * @since 2020-12-08 21:47:27
 */
@Data
public class ScanCodeLogin implements Serializable {
    private static final long serialVersionUID = 952679874611008471L;
    /**
     * scan code id(系统自动生成且不会变化)
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 扫描二维码
     */
    private String scanCode;

    /**
     * 扫描二维码状态，0:已创建(待扫描)，1:已扫描(待确认)，2:已确认(待登录)，3:登录成功，4:已失效
     */
    private Integer scanCodeState;

    /**
     * 扫描二维码过期时间(生成二维码 -> 扫码请求时间)
     */
    private Date expiredTime;

    /**
     * 绑定的JWT-Token id
     */
    private Long bindTokenId;

    /**
     * (扫描时间)绑定JWT-Token时间
     */
    private Date bindTokenTime;

    /**
     * 确认登录过期时间(扫码二维码 -> 确认登录时间)
     */
    private Date confirmExpiredTime;

    /**
     * 确认登录时间
     */
    private Date confirmTime;

    /**
     * 获取登录JWT-Token过期时间(确认登录 -> 获取登录Token时间)
     */
    private Date getTokenExpiredTime;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录生成的JWT-Token id
     */
    private Long tokenId;

    /**
     * 扫描二维码失效原因
     */
    private String invalidReason;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

}