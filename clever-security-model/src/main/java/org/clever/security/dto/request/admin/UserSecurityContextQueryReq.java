package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/18 15:28 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserSecurityContextQueryReq extends QueryByPage {
    /**
     * 域id
     */
    private Long domainId;

    /**
     * uid、login_name、telephone、email、nickname
     */
    private String userSearchKey;

    /**
     * 创建时间
     */
    private Date createAtStart;

    /**
     * 创建时间
     */
    private Date createAtEnd;
}
