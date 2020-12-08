package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.clever.security.entity.ScanCodeLogin;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/08 21:48 <br/>
 */
@Repository
@Mapper
public interface ScanCodeLoginMapper extends BaseMapper<ScanCodeLogin> {
}
