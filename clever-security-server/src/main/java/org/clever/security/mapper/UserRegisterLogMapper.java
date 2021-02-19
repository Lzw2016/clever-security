package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.dto.request.admin.UserRegisterLogQueryReq;
import org.clever.security.dto.response.admin.UserRegisterLogQueryRes;
import org.clever.security.entity.UserRegisterLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/09 14:38 <br/>
 */
@Repository
@Mapper
public interface UserRegisterLogMapper extends BaseMapper<UserRegisterLog> {

    List<UserRegisterLogQueryRes> pageQuery(@Param("query") UserRegisterLogQueryReq req);
}
