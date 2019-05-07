package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.entity.WebPermission;
import org.clever.security.entity.model.WebPermissionModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:13 <br/>
 */
@Repository
@Mapper
public interface WebPermissionMapper extends BaseMapper<WebPermission> {

    WebPermissionModel getBySysNameAndTarget(
            @Param("sysName") String sysName,
            @Param("targetClass") String targetClass,
            @Param("targetMethod") String targetMethod,
            @Param("targetMethodParams") String targetMethodParams
    );

    List<WebPermissionModel> findBySysName(@Param("sysName") String sysName);
}
