package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.entity.Permission;
import org.clever.security.entity.User;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:08 <br/>
 */
public interface UserMapper extends BaseMapper<User> {

    User getByUsername(@Param("username") String username);

    User getByTelephone(@Param("telephone") String telephone);

    List<Permission> findByUsername(@Param("username") String username);
}
