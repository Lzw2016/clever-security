package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.dto.request.admin.ScanCodeLoginQueryReq;
import org.clever.security.dto.response.admin.ScanCodeLoginQueryRes;
import org.clever.security.mapper.ScanCodeLoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/17 20:25 <br/>
 */
@Transactional(readOnly = true)
@Service
public class ScanCodeLoginService {
    @Autowired
    private ScanCodeLoginMapper scanCodeLoginMapper;

    public IPage<ScanCodeLoginQueryRes> pageQuery(ScanCodeLoginQueryReq req) {
        req.addOrderFieldMapping("id", "a.id");
        req.addOrderFieldMapping("domainId", "a.domain_id");
        req.addOrderFieldMapping("scanCode", "a.scan_code");
        req.addOrderFieldMapping("scanCodeState", "a.scan_code_state");
        req.addOrderFieldMapping("expiredTime", "a.expired_time");
        req.addOrderFieldMapping("bindTokenId", "a.bind_token_id");
        req.addOrderFieldMapping("bindTokenTime", "a.bind_token_time");
        req.addOrderFieldMapping("confirmExpiredTime", "a.confirm_expired_time");
        req.addOrderFieldMapping("confirmTime", "a.confirm_time");
        req.addOrderFieldMapping("getTokenExpiredTime", "a.get_token_expired_time");
        req.addOrderFieldMapping("loginTime", "a.login_time");
        req.addOrderFieldMapping("tokenId", "a.token_id");
        req.addOrderFieldMapping("createAt", "a.create_at");
        req.addOrderFieldMapping("updateAt", "a.update_at");
        if (req.getOrderFields().isEmpty()) {
            req.addOrderField("createAt", DomainQueryReq.DESC);
        }
        return req.result(scanCodeLoginMapper.pageQuery(req));
    }
}
