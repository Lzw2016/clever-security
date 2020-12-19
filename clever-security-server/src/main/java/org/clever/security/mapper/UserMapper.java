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
}
