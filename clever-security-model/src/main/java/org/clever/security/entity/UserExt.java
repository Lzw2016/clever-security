package org.clever.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户扩展表(UserExt)实体类
 *
 * @author lizw
 * @since 2020-12-13 20:59:30
 */
@Data
public class UserExt implements Serializable {
    private static final long serialVersionUID = -28090812452997640L;
    /**
     * 域id
     */
    @TableId(type = IdType.INPUT)
    private Long domainId;
    /**
     * 用户id(系统自动生成且不会变化)
     */
    // @TableId(type = IdType.INPUT)
    private String uid;

    /**
     * 微信openId
     */
    private String wechatOpenId;

    /**
     * 微信unionId
     */
    private String wechatUnionId;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

}