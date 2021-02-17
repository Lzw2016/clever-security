package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 21:51 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ScanCodeLoginQueryReq extends QueryByPage {
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
     * (扫描时间)绑定JWT-Token时间 - 开始
     */
    private Date bindTokenTimeStart;

    /**
     * (扫描时间)绑定JWT-Token时间 - 结束
     */
    private Date bindTokenTimeEnd;

    /**
     * 确认登录时间 - 开始
     */
    private Date confirmTimeStart;

    /**
     * 确认登录时间 - 结束
     */
    private Date confirmTimeEnd;

    /**
     * 登录时间 - 开始
     */
    private Date loginTimeStart;

    /**
     * 登录时间 - 结束
     */
    private Date loginTimeEnd;

    /**
     * 创建时间 - 开始
     */
    private Date createAtStart;

    /**
     * 创建时间 - 结束
     */
    private Date createAtEnd;
}
