package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/18 14:06 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ValidateCodeQueryReq extends QueryByPage {
    /**
     * 域id
     */
    private Long domainId;

    /**
     * uid、login_name、telephone、email、nickname
     */
    private String userSearchKey;

    /**
     * 验证码类型
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
     * 创建时间
     */
    private Date createAtStart;

    /**
     * 创建时间
     */
    private Date createAtEnd;
}
