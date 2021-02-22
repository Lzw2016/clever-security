package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.clever.security.dto.request.admin.ScanCodeLoginQueryReq;
import org.clever.security.dto.response.admin.ScanCodeLoginQueryRes;
import org.clever.security.entity.ScanCodeLogin;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/08 21:48 <br/>
 */
@Repository
@Mapper
public interface ScanCodeLoginMapper extends BaseMapper<ScanCodeLogin> {
    @Select("select * from scan_code_login where domain_id=#{domainId} and scan_code=#{scanCode}")
    ScanCodeLogin getByScanCode(@Param("domainId") Long domainId, @Param("scanCode") String scanCode);

    List<ScanCodeLoginQueryRes> pageQuery(@Param("query") ScanCodeLoginQueryReq query);

    @Delete({
            "delete from scan_code_login ",
            "where (scan_code_state=4 or expired_time<now() or confirm_expired_time<now() or get_token_expired_time<now() or token_id is not null) ",
            "and create_at<date_sub(now(), interval #{retainOfDays} day)",
    })
    int clearLogData(@Param("retainOfDays") int retainOfDays);

    @Update({
            "update scan_code_login set scan_code_state=4, invalid_reason='二维码已过期' ",
            "where scan_code_state!=4 and (expired_time<now() or confirm_expired_time<now() or get_token_expired_time<now())",
    })
    int refreshState();
}
