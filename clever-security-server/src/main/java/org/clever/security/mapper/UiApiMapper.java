package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.clever.security.entity.UiApi;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/03/04 16:01 <br/>
 */
@Repository
@Mapper
public interface UiApiMapper extends BaseMapper<UiApi> {
}
