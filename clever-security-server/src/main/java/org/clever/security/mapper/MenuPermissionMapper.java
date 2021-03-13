package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clever.security.dto.request.admin.MenuPermissionQueryReq;
import org.clever.security.dto.response.admin.MenuPermissionQueryRes;
import org.clever.security.dto.response.admin.MenuPermissionTreeRes;
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

    List<MenuPermissionTreeRes> menuTree(@Param("domainId") Long domainId);

    List<MenuPermissionQueryRes> pageQuery(@Param("query") MenuPermissionQueryReq req);

    @Select("select count(1) from menu_permission where id = #{id} limit 1")
    int exists(@Param("id") Long id);

    int delByIds(@Param("ids") List<Long> ids);
}
