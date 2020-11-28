package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.clever.security.entity.UserLoginLog;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:07 <br/>
 */
@Repository
@Mapper
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {
}
