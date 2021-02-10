package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:06 <br/>
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdatePasswordRes extends BaseResponse {
    /**
     * 是否成功
     */
    private boolean success;

    public UpdatePasswordRes(boolean success) {
        this.success = success;
    }
}
