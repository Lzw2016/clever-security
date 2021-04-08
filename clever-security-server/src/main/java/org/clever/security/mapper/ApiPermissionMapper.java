package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.dto.request.admin.ApiPermissionQueryReq;
import org.clever.security.dto.response.GetApiPermissionRes;
import org.clever.security.dto.response.admin.ApiPermissionQueryRes;
import org.clever.security.entity.ApiPermission;
import org.clever.security.model.auth.ApiPermissionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    List<ApiPermissionEntity> findApiPermissionBydDomainId(@Param("domainId") Long domainId);

    List<ApiPermissionQueryRes> findApiByMenu(@Param("menuId") Long menuId);

    List<ApiPermissionQueryRes> findApiByUi(@Param("uiId") Long uiId);

    List<ApiPermissionQueryRes> pageQuery(@Param("query") ApiPermissionQueryReq req);
}
