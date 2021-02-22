package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.dto.request.admin.ScanCodeLoginQueryReq;
import org.clever.security.dto.response.admin.ScanCodeLoginDetailRes;
import org.clever.security.dto.response.admin.ScanCodeLoginQueryRes;
import org.clever.security.entity.JwtToken;
import org.clever.security.entity.ScanCodeLogin;
import org.clever.security.entity.User;
import org.clever.security.mapper.DomainMapper;
import org.clever.security.mapper.JwtTokenMapper;
import org.clever.security.mapper.ScanCodeLoginMapper;
import org.clever.security.mapper.UserMapper;
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
    @Autowired
    private JwtTokenMapper jwtTokenMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DomainMapper domainMapper;

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

    public ScanCodeLoginDetailRes detailScanCodeLogin(Long id) {
        ScanCodeLoginDetailRes res = new ScanCodeLoginDetailRes();
        ScanCodeLogin scanCodeLogin = scanCodeLoginMapper.selectById(id);
        if (scanCodeLogin == null) {
            throw new BusinessException("数据不存在");
        }
        res.setScanCodeLogin(scanCodeLogin);
        if (scanCodeLogin.getDomainId() != null) {
            res.setDomain(domainMapper.selectById(scanCodeLogin.getDomainId()));
        }
        if (scanCodeLogin.getBindTokenId() != null) {
            JwtToken bindToken = jwtTokenMapper.selectById(scanCodeLogin.getBindTokenId());
            res.setBindToken(bindToken);
            if (bindToken != null && StringUtils.isNotBlank(bindToken.getUid())) {
                User bindTokenUser = userMapper.getByUid(bindToken.getUid());
                if(bindTokenUser!=null) {
                    bindTokenUser.setPassword("******");
                }
                res.setBindTokenUser(bindTokenUser);
            }
        }
        if (scanCodeLogin.getTokenId() != null) {
            JwtToken token = jwtTokenMapper.selectById(scanCodeLogin.getTokenId());
            res.setToken(token);
            if (token != null && StringUtils.isNotBlank(token.getUid())) {
                User tokenUser = userMapper.getByUid(token.getUid());
                if(tokenUser!=null) {
                    tokenUser.setPassword("******");
                }
                res.setTokenUser(tokenUser);
            }
        }
        return res;
    }

    @Transactional
    public int clearLogData(int retainOfDays) {
        if (retainOfDays <= 0) {
            return 0;
        }
        return scanCodeLoginMapper.clearLogData(retainOfDays);
    }

    @Transactional
    public int refreshState() {
        return scanCodeLoginMapper.refreshState();
    }
}
