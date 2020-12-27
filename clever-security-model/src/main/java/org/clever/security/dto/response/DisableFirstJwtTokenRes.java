package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 21:06 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DisableFirstJwtTokenRes extends BaseResponse {
    /**
     * 用户id
     */
    private String uid;

    /**
     * 禁用Token数量
     */
    private Integer disableCount;
}
