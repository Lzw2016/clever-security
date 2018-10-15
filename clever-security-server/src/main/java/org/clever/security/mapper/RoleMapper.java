package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.clever.security.dto.request.RoleQueryPageReq;
import org.clever.security.entity.Role;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:11 <br/>
 */
public interface RoleMapper extends BaseMapper<Role> {

    Role getByName(@Param("name") String name);

    List<Role> findByUsername(@Param("username") String username);

    List<Role> findByPage(@Param("query") RoleQueryPageReq query, IPage page);

    int updateUserRoleByRoleName(@Param("oldName") String oldName, @Param("newName") String newName);

    int updateRolePermissionByRoleName(@Param("oldName") String oldName, @Param("newName") String newName);

    int delUserRoleByRoleName(@Param("name") String name);

    int delRolePermissionByRoleName(@Param("name") String name);
}
