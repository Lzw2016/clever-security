package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clever.security.dto.request.admin.RoleQueryReq;
import org.clever.security.dto.response.admin.RoleQueryRes;
import org.clever.security.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:04 <br/>
 */
@Repository
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    Set<String> findRolesByUid(@Param("domainId") Long domainId, @Param("uid") String uid);

    List<RoleQueryRes> pageQuery(@Param("query") RoleQueryReq req);

    @Select("select * from role where domain_id=#{domainId} and id=#{id} limit 1")
    Role getByDomainId(@Param("domainId") Long domainId, @Param("id") Long id);
}
