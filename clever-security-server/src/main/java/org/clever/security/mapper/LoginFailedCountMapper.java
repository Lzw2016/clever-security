package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.clever.security.dto.request.admin.LoginFailedCountQueryReq;
import org.clever.security.dto.response.admin.LoginFailedCountQueryRes;
import org.clever.security.entity.LoginFailedCount;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/19 16:57 <br/>
 */
@Repository
@Mapper
public interface LoginFailedCountMapper extends BaseMapper<LoginFailedCount> {

    @Select("select * from login_failed_count where delete_flag=0 and domain_id=#{domainId} and uid=#{uid} and login_type=#{loginType} ")
    LoginFailedCount getByUidAndLoginType(@Param("domainId") Long domainId, @Param("uid") String uid, @Param("loginType") Integer loginType);

    @Update("update login_failed_count set failed_count=failed_count+1, last_login_time=now() where delete_flag=0 and domain_id=#{domainId} and uid=#{uid} and login_type=#{loginType} ")
    int addLoginFailedCount(@Param("domainId") Long domainId, @Param("uid") String uid, @Param("loginType") Integer loginType);

    @Update("update login_failed_count set delete_flag=1 where delete_flag=0 and domain_id=#{domainId} and uid=#{uid} and login_type=#{loginType} ")
    int clearLoginFailedCount(@Param("domainId") Long domainId, @Param("uid") String uid, @Param("loginType") Integer loginType);

    List<LoginFailedCountQueryRes> pageQuery(@Param("query") LoginFailedCountQueryReq req);

    @Delete("delete from login_failed_count where delete_flag=1 and create_at<date_sub(now(), interval #{retainOfDays} day)")
    int clearLogData(@Param("retainOfDays") int retainOfDays);
}
