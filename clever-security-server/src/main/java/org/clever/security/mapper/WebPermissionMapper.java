package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.entity.WebPermission;
import org.clever.security.entity.model.WebPermissionModel;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:13 <br/>
 */
public interface WebPermissionMapper extends BaseMapper<WebPermission> {

    WebPermissionModel getBySysNameAndController(
            @Param("sysName") String sysName,
            @Param("controllerClass") String controllerClass,
            @Param("controllerMethod") String controllerMethod,
            @Param("controllerMethodParams") String controllerMethodParams
    );

    List<WebPermissionModel> findBySysName(@Param("sysName") String sysName);
}
