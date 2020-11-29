package org.clever.security.model.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

/**
 * 用户登录请求
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/11/29 14:32 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractUserLoginReq extends BaseRequest {

}
