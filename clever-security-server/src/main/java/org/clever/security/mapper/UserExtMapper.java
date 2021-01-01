package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clever.security.entity.UserExt;
import org.springframework.stereotype.Repository;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/19 20:44 <br/>
 */
@Repository
@Mapper
public interface UserExtMapper extends BaseMapper<UserExt> {
    @Select("select * from user_ext where domain_id=#{domainId} and wechat_open_id=#{wechatOpenId}")
    UserExt getByWechatOpenId(@Param("domainId") Long domainId, @Param("wechatOpenId") String wechatOpenId);
}
