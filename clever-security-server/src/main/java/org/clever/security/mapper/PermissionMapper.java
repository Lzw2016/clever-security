package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.entity.Permission;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-17 9:13 <br/>
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> findByUsername(@Param("username") String username);
}
