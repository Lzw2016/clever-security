package org.clever.security.model.register;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.request.BaseRequest;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/01 20:41 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractUserRegisterReq extends BaseRequest {
    /**
     * 用户注册渠道，0:管理员，1:PC-Web，2:H5，3:IOS-APP，4:Android-APP，5:微信小程序
     */
    private Integer registerChannel;
}
