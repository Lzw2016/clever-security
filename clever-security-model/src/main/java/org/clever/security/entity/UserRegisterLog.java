package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户注册日志(UserRegisterLog)实体类
 *
 * @author lizw
 * @since 2021-01-09 14:36:19
 */
@Data
public class UserRegisterLog implements Serializable {
    private static final long serialVersionUID = 901975732052118284L;
    /**
     * 注册日志id(系统自动生成且不会变化)
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 注册的域id
     */
    private Long registerDomainId;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 注册IP
     */
    private String registerIp;

    /**
     * 注册渠道，0:PC-Admin，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private Integer registerChannel;

    /**
     * 注册类型，1:登录名注册，2:手机号注册，3:邮箱注册，4:微信小程序注册，
     */
    private Integer registerType;

    /**
     * 注册请求数据
     */
    private String requestData;

    /**
     * 注册结果，0:注册失败，1:注册成功且创建用户，2:注册成功仅关联到域
     */
    private Integer requestResult;

    /**
     * 注册成功的用户id
     */
    private String registerUid;

    /**
     * 注册失败原因
     */
    private String failReason;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

}