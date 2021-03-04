package org.clever.security.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 页面UI-API权限表(UiApi)实体类
 *
 * @author lizw
 * @since 2021-03-04 15:59:26
 */
@Data
public class UiApi implements Serializable {
    private static final long serialVersionUID = -15667015649415928L;
    /**
     * 页面ui id(ui_permission.id)
     */
    @TableId(type = IdType.INPUT)
    private Long uiId;

    /**
     * api id(api_permission.id)
     */
    private Long apiId;

    /**
     * 域id
     */
    private Long domainId;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;
}
