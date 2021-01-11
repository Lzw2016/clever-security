package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.clever.security.entity.UserRegisterLog;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 14:38 <br/>
 */
@Repository
@Mapper
public interface UserRegisterLogMapper extends BaseMapper<UserRegisterLog> {
}
