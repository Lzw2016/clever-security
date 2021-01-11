package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.clever.security.entity.ValidateCode;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/28 20:08 <br/>
 */
@Repository
@Mapper
public interface ValidateCodeMapper extends BaseMapper<ValidateCode> {

    @Select("select * from validate_code where domain_id=#{domainId} and type=#{type} and send_channel=#{sendChannel} and digest=#{digest} and (send_channel=0 or send_target=#{sendTarget})")
    ValidateCode getByDigest(
            @Param("domainId") Long domainId,
            @Param("type") Integer type,
            @Param("sendChannel") Integer sendChannel,
            @Param("digest") String digest,
            @Param("sendTarget") String sendTarget
    );

    @Select("select count(1) from validate_code where domain_id=#{domainId} and uid=#{uid} and type=#{type} and send_channel=#{sendChannel} and create_at>=#{start} and create_at<=#{end}")
    int getSendCount(
            @Param("domainId") Long domainId,
            @Param("uid") String uid,
            @Param("type") Integer type,
            @Param("sendChannel") Integer sendChannel,
            @Param("start") Date start,
            @Param("end") Date end
    );

    @Select({
            "select * from validate_code where validate_time is null and expired_time>=date_add(now(), interval 15 second) ",
            "and domain_id=#{domainId} and uid=#{uid} and type=#{type} and send_channel=#{sendChannel} and send_target=#{sendTarget} order by create_at desc limit 1"
    })
    ValidateCode getLastEffective(
            @Param("domainId") Long domainId,
            @Param("uid") String uid,
            @Param("type") Integer type,
            @Param("sendChannel") Integer sendChannel,
            @Param("sendTarget") String sendTarget
    );

    @Select("select count(1) from validate_code where domain_id=#{domainId} and type=#{type} and send_channel=#{sendChannel} and send_target=#{sendTarget} and create_at>=#{start} and create_at<=#{end}")
    int getSendCountNoUid(
            @Param("domainId") Long domainId,
            @Param("type") Integer type,
            @Param("sendChannel") Integer sendChannel,
            @Param("sendTarget") String sendTarget,
            @Param("start") Date start,
            @Param("end") Date end
    );

    @Select({
            "select * from validate_code where validate_time is null and expired_time>=date_add(now(), interval 15 second) ",
            "and domain_id=#{domainId} and type=#{type} and send_channel=#{sendChannel} and send_target=#{sendTarget} order by create_at desc limit 1"
    })
    ValidateCode getLastEffectiveNoUid(
            @Param("domainId") Long domainId,
            @Param("type") Integer type,
            @Param("sendChannel") Integer sendChannel,
            @Param("sendTarget") String sendTarget
    );
}
