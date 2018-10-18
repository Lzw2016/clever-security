package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.entity.User;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-07 21:02 <br/>
 */
public interface QueryMapper extends BaseMapper<User> {

    List<String> allSysName();

    List<String> allRoleName();

    List<String> findRoleNameByUser(@Param("username") String username);

    List<String> findPermissionStrByUser(@Param("roleName") String roleName);
}
