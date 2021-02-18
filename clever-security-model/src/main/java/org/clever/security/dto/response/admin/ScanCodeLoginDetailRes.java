package org.clever.security.dto.response.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;
import org.clever.security.entity.Domain;
import org.clever.security.entity.JwtToken;
import org.clever.security.entity.ScanCodeLogin;
import org.clever.security.entity.User;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/18 12:58 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ScanCodeLoginDetailRes extends BaseResponse {

    private Domain domain;

    private ScanCodeLogin scanCodeLogin;

    private JwtToken bindToken;

    private User bindTokenUser;

    private JwtToken token;

    private User tokenUser;
}
