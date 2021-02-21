package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.dto.request.admin.MenuPermissionQueryReq;
import org.clever.security.dto.response.admin.MenuPermissionQueryRes;
import org.clever.security.entity.MenuPermission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:02 <br/>
 */
@Repository
@Mapper
public interface MenuPermissionMapper extends BaseMapper<MenuPermission> {

    List<MenuPermissionQueryRes> pageQuery(@Param("query") MenuPermissionQueryReq req);
}
