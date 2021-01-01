package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.clever.security.entity.JwtToken;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:02 <br/>
 */
@Repository
@Mapper
public interface JwtTokenMapper extends BaseMapper<JwtToken> {
    @Select("select * from jwt_token where (expired_time is not null and expired_time>now()) and disable=0 and id=#{id}")
    JwtToken getEffectiveTokenById(@Param("id") Long id);

    @Select("select count(1) from jwt_token where (expired_time is not null and expired_time>now()) and disable=0 and domain_id=#{domainId} and uid=#{uid}")
    int getConcurrentLoginCount(@Param("domainId") Long domainId, @Param("uid") String uid);

    @Select("select * from jwt_token where (expired_time is not null and expired_time>now()) and disable=0 and domain_id=#{domainId} and uid=#{uid} order by create_at limit #{count}")
    List<JwtToken> getFirstJwtToken(@Param("domainId") Long domainId, @Param("uid") String uid, @Param("count") Integer count);

    @Update("update jwt_token set disable=1, disable_reason=#{disableReason} where domain_id=#{domainId} and id=#{id}")
    int disableJwtToken(@Param("domainId") Long domainId, @Param("id") Long id, @Param("disableReason") String disableReason);
}
