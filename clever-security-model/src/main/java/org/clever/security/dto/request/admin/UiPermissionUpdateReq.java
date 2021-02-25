package org.clever.security.dto.request.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.QueryByPage;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:55 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UiPermissionUpdateReq extends QueryByPage {
    /**
     * id
     */
    private Long id;
    /**
     * 域id
     */
    private Long domainId;
    /**
     * 关键字搜索
     */
    private String uiName;
}