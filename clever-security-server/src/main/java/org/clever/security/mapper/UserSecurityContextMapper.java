package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.clever.security.entity.UserSecurityContext;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/19 12:56 <br/>
 */
@Repository
@Mapper
public interface UserSecurityContextMapper extends BaseMapper<UserSecurityContext> {

    @Select("select * from user_security_context where domain_id=#{domainId} and uid=#{uid}")
    UserSecurityContext getByUid(@Param("domainId") Long domainId, @Param("uid") String uid);

    @Delete("delete from user_security_context where domain_id=#{domainId} and uid=#{uid}")
    int deleteByDomainIdAndUid(@Param("domainId") Long domainId, @Param("uid") String uid);

    @Update("update user_security_context set security_context=#{securityContext} where id=#{id}")
    int updateSecurityContextById(@Param("id") Long id, @Param("securityContext") String securityContext);

    @Delete("delete from user_security_context where uid=#{uid}")
    int deleteByUid(@Param("uid") String uid);
}
