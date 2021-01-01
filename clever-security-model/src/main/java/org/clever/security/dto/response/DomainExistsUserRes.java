package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 17:53 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DomainExistsUserRes extends BaseResponse {
    /**
     * 用户是否在域中
     */
    private boolean exists;
    /**
     * 用户id
     */
    private String uid;
    /**
     * 用户登录名
     */
    private String loginName;
}
