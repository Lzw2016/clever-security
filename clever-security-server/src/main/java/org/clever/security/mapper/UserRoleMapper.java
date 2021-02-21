package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.entity.UserRole;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:07 <br/>
 */
@Repository
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Delete("delete from user_role where role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") Long roleId);
}
