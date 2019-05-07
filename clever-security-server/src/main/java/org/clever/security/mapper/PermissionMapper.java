package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.dto.request.PermissionQueryReq;
import org.clever.security.entity.Permission;
import org.clever.security.entity.model.WebPermissionModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:13 <br/>
 */
@Repository
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> findByUsername(@Param("username") String username);

    List<Permission> findByRoleName(@Param("roleName") String roleName);

    List<WebPermissionModel> findByPage(@Param("query") PermissionQueryReq query, IPage page);

    int existsPermission(@Param("permissionStr") String permissionStr);

    WebPermissionModel getByPermissionStr(@Param("permissionStr") String permissionStr);

    int delRolePermission(@Param("permissionStr") String permissionStr);
}
