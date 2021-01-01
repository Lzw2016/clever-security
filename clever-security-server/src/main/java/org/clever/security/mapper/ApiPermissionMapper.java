package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clever.security.dto.response.GetApiPermissionRes;
import org.clever.security.entity.ApiPermission;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 19:56 <br/>
 */
@Repository
@Mapper
public interface ApiPermissionMapper extends BaseMapper<ApiPermission> {

    GetApiPermissionRes getByTargetMethod(
            @Param("domainId") Long domainId,
            @Param("className") String className,
            @Param("methodName") String methodName,
            @Param("methodParams") String methodParams);
}
