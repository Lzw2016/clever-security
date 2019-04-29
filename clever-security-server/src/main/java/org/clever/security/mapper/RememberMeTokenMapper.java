package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.entity.RememberMeToken;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * “记住我”功能的token(RememberMeToken)表数据库访问层
 *
 * @author lizw
 * @since 2018-09-21 20:10:31
 */
@Repository
@Mapper
public interface RememberMeTokenMapper extends BaseMapper<RememberMeToken> {

    int updateBySeries(@Param("series") String series, @Param("tokenValue") String tokenValue, @Param("lastUsed") Date lastUsed);

    RememberMeToken getBySeries(@Param("series") String series);

    int deleteByUsername(@Param("username") String username);

    int deleteBySysNameAndUsername(@Param("sysName") String sysName, @Param("username") String username);
}