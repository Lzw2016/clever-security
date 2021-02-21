package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.entity.RolePermission;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:04 <br/>
 */
@Repository
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    @Delete("delete from role_permission where role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") Long roleId);
}
