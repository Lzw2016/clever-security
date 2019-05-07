package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.entity.ServiceSys;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-22 15:23 <br/>
 */
@Repository
@Mapper
public interface ServiceSysMapper extends BaseMapper<ServiceSys> {

    int existsSysName(@Param("sysName") String sysName);

    int existsRedisNameSpace(@Param("redisNameSpace") String redisNameSpace);

    ServiceSys getByUnique(@Param("sysName") String sysName, @Param("redisNameSpace") String redisNameSpace);

    ServiceSys getBySysName(@Param("sysName") String sysName);

    List<String> allSysName();
}
