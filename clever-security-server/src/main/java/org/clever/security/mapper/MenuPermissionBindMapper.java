package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.clever.security.entity.MenuPermissionBind;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/14 23:12 <br/>
 */
@Repository
@Mapper
public interface MenuPermissionBindMapper extends BaseMapper<MenuPermissionBind> {
}
