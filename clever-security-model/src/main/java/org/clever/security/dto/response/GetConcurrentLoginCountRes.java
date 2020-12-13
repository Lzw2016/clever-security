package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 17:11 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetConcurrentLoginCountRes extends BaseResponse {
    /**
     * 用户id
     */
    private String uid;
    /**
     * 并发登录数量
     */
    private int concurrentLoginCount;
}
