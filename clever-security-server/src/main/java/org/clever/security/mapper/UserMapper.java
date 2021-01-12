package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clever.security.entity.User;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:06 <br/>
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where uid=#{uid}")
    User getByUid(@Param("uid") String uid);

    @Select("select * from user where login_name=#{loginName}")
    User getByLoginName(@Param("loginName") String loginName);

    @Select("select * from user where telephone=#{telephone}")
    User getByTelephone(@Param("telephone") String telephone);

    @Select("select * from user where email=#{email}")
    User getByEmail(@Param("email") String email);

    @Select("select count(1) from user where uid=#{uid} limit 1")
    int existsUid(@Param("uid") String uid);
}
