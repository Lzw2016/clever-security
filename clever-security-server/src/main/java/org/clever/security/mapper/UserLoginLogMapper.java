package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.entity.UserLoginLog;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-23 16:02 <br/>
 */
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {

    UserLoginLog getBySessionId(@Param("sessionId") String sessionId);
}
