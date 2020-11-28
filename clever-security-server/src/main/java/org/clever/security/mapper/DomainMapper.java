package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.clever.security.entity.Domain;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:01 <br/>
 */
@Repository
@Mapper
public interface DomainMapper extends BaseMapper<Domain> {
}
