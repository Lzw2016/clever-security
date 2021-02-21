package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.dto.request.admin.UiPermissionQueryReq;
import org.clever.security.dto.response.admin.UiPermissionQueryRes;
import org.clever.security.entity.UiPermission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:05 <br/>
 */
@Repository
@Mapper
public interface UiPermissionMapper extends BaseMapper<UiPermission> {

    List<UiPermissionQueryRes> pageQuery(@Param("query") UiPermissionQueryReq req);
}
