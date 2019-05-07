package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.dto.request.RoleQueryPageReq;
import org.clever.security.entity.Permission;
import org.clever.security.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:11 <br/>
 */
@Repository
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    Role getByName(@Param("name") String name);

    List<Role> findByUsername(@Param("username") String username);

    List<Role> findByPage(@Param("query") RoleQueryPageReq query, IPage page);

    int updateUserRoleByRoleName(@Param("oldName") String oldName, @Param("newName") String newName);

    int updateRolePermissionByRoleName(@Param("oldName") String oldName, @Param("newName") String newName);

    int updateRolePermissionByPermissionStr(@Param("oldPermissionStr") String oldPermissionStr, @Param("newPermissionStr") String newPermissionStr);

    int delUserRoleByRoleName(@Param("name") String name);

    int delRolePermissionByRoleName(@Param("name") String name);

    int addPermission(@Param("roleName") String roleName, @Param("permissionStr") String permissionStr);

    int delPermission(@Param("roleName") String roleName, @Param("permissionStr") String permissionStr);

    int existsByRole(@Param("roleName") String roleName);

    List<Permission> findPermissionByRoleName(@Param("roleName") String roleName);

    int existsRolePermission(@Param("roleName") String roleName, @Param("permissionStr") String permissionStr);

    List<String> findUsernameByRoleName(@Param("roleName") String roleName);

    List<String> findRoleNameByPermissionStr(@Param("permissionStr") String permissionStr);

    List<String> findRoleNameByPermissionStrList(@Param("permissionStrList") Set<String> permissionStrList);
}
