package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clever.security.dto.request.admin.ServerAccessTokenQueryReq;
import org.clever.security.dto.response.admin.ServerAccessTokenQueryRes;
import org.clever.security.entity.ServerAccessToken;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/23 17:10 <br/>
 */
@Repository
@Mapper
public interface ServerAccessTokenMapper extends BaseMapper<ServerAccessToken> {
    @Select("select * from server_access_token where disable=0 and (expired_time is null or expired_time>now()) ")
    List<ServerAccessToken> findAllEffective();

    List<ServerAccessTokenQueryRes> pageQuery(@Param("query") ServerAccessTokenQueryReq query);
}
