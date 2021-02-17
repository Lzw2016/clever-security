package org.clever.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
}
