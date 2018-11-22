package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.clever.security.dto.request.UserQueryPageReq;
import org.clever.security.entity.Permission;
import org.clever.security.entity.Role;
import org.clever.security.entity.ServiceSys;
import org.clever.security.entity.User;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:08 <br/>
 */
public interface UserMapper extends BaseMapper<User> {

    User getByUnique(@Param("unique") String unique);

    User getByUsername(@Param("username") String username);

    User getByTelephone(@Param("telephone") String telephone);

    List<Permission> findByUsername(@Param("username") String username, @Param("sysName") String sysName);

    int existsUserBySysName(@Param("username") String username, @Param("sysName") String sysName);

    List<User> findByPage(@Param("query") UserQueryPageReq query, IPage page);

    List<String> findSysNameByUsername(@Param("username") String username);

    List<ServiceSys> findSysByUsername(@Param("username") String username);

    int addUserSys(@Param("username") String username, @Param("sysName") String sysName);

    int delUserSys(@Param("username") String username, @Param("sysName") String sysName);

    int existsByUserName(@Param("username") String username);

    int existsByTelephone(@Param("telephone") String telephone);

    int existsByEmail(@Param("email") String email);

    int addRole(@Param("username") String username, @Param("roleName") String roleName);

    int delRole(@Param("username") String username, @Param("roleName") String roleName);

    List<Role> findRoleByUsername(@Param("username") String username);

    int existsUserRole(@Param("username") String username, @Param("roleName") String roleName);

    int delUserRole(@Param("username") String username);

    int delAllUserSys(@Param("username") String username);
}
