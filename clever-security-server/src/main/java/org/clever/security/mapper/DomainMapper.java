package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.entity.Domain;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:01 <br/>
 */
@Repository
@Mapper
public interface DomainMapper extends BaseMapper<Domain> {

    List<Domain> pageQuery(@Param("query") DomainQueryReq query);

    @Select("select * from domain where name=#{name} limit 1")
    Domain getByName(@Param("name") String name);

    @Select("select * from domain where redis_name_space=#{redisNameSpace} limit 1")
    Domain getByRedisNameSpace(@Param("redisNameSpace") String redisNameSpace);

    @Select("select count(1) from domain where id = #{domainId} limit 1")
    int exists(@Param("domainId") Long domainId);
}
