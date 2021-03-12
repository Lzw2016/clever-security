package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clever.security.entity.Permission;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:03 <br/>
 */
@Repository
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    Set<String> findPermissionByUid(@Param("domainId") Long domainId, @Param("uid") String uid);

    @Select("select count(1) from permission where str_flag=#{strFlag} limit 1")
    int strFlagExist(@Param("strFlag") String strFlag);
}
