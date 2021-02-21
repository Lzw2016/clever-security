package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clever.security.entity.UserDomain;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:06 <br/>
 */
@Repository
@Mapper
public interface UserDomainMapper extends BaseMapper<UserDomain> {
    @Select("select count(1) from user_domain where domain_id=#{domainId} and uid=#{uid} limit 1")
    int exists(@Param("domainId") Long domainId, @Param("uid") String uid);

    @Delete("delete from user_domain where domain_id=#{domainId} and uid=#{uid}")
    void deleteByDomainId(@Param("domainId") Long domainId, @Param("uid") String uid);
}
