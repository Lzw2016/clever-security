package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clever.security.dto.request.RememberMeTokenQueryReq;
import org.clever.security.dto.request.ServiceSysQueryReq;
import org.clever.security.dto.request.UserLoginLogQueryReq;
import org.clever.security.entity.ServiceSys;
import org.clever.security.entity.User;
import org.clever.security.entity.model.UserLoginLogModel;
import org.clever.security.entity.model.UserRememberMeToken;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-07 21:02 <br/>
 */
@Repository
@Mapper
public interface QueryMapper extends BaseMapper<User> {

    List<String> allSysName();

    List<String> allRoleName();

    List<String> findRoleNameByUser(@Param("username") String username);

    List<String> findPermissionStrByRole(@Param("roleName") String roleName);

    List<UserRememberMeToken> findRememberMeToken(@Param("query") RememberMeTokenQueryReq query, IPage page);

    List<UserLoginLogModel> findUserLoginLog(@Param("query") UserLoginLogQueryReq query, IPage page);

    List<ServiceSys> findServiceSys(@Param("query") ServiceSysQueryReq query, IPage page);
}
