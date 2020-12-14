package org.clever.security.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.clever.common.model.response.BaseResponse;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/14 21:06 <br/>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DisableFirstJwtTokenRes extends BaseResponse {
    /**
     * JWT-Token id(系统自动生成且不会变化)
     */
    private Long id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * token数据
     */
    private String token;

    /**
     * JWT-Token过期时间(空表示永不过期)
     */
    private Date expiredTime;

    /**
     * JWT-Token是否禁用，0:未禁用, 1:已禁用
     */
    private Integer disable;

    /**
     * 创建时间
     */
    private Date createAt;
}
