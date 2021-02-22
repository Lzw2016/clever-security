package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.clever.security.dto.request.admin.JwtTokenQueryReq;
import org.clever.security.dto.response.admin.JwtTokenQueryRes;
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

    @Update("update jwt_token set disable=1, disable_reason=#{disableReason} where (expired_time is not null and expired_time>now()) and disable=0 and uid=#{uid}")
    int disableJwtTokenByUid(@Param("uid") String uid, @Param("disableReason") String disableReason);

    @Select("select * from jwt_token where (expired_time is not null and expired_time>now()) and disable=0 and uid=#{uid}")
    List<JwtToken> getEffectiveTokenByUid(@Param("uid") String uid);

    List<JwtTokenQueryRes> pageQuery(@Param("query")JwtTokenQueryReq query);

    @Delete({
            "delete from jwt_token ",
            "where (expired_time<now() or disable!=0 or refresh_create_token_id is not null) ",
            "and create_at<date_sub(now(), interval #{retainOfDays} day)",
    })
    int clearLogData(@Param("retainOfDays") int retainOfDays);

    @Update("update jwt_token set disable=1, disable_reason='jwt-token已过期' where disable!=1 and expired_time<now()")
    int refreshState();
}
