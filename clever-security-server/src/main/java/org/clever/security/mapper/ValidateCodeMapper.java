package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.clever.security.entity.ValidateCode;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:08 <br/>
 */
@Repository
@Mapper
public interface ValidateCodeMapper extends BaseMapper<ValidateCode> {
}
