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
public class UserRegisterLogQueryReq extends QueryByPage {
    /**
     * id
     */
    private Long id;
    /**
     * 域id
     */
    private Long domainId;
    /**
     * 注册ip
     */
   private String registerIp;
    /**
     * 注册渠道
     */
   private Integer registerChannel;
    /**
     * 注册类型
     */
   private Integer registerType;
    /**
     * 注册结果
     */
   private Integer requestResult;
    /**
     * 用户账号关键字
     */
   private String userKeyword;
    /**
     * 注册时间 - 开始
     */
    private Date registerTimeStart;

    /**
     * 注册时间 - 结束
     */
    private Date registerTimeEnd;
}