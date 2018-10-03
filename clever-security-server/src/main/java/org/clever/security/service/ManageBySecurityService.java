package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.dto.request.UserBindRoleReq;
import org.clever.security.dto.request.UserBindSysReq;
import org.clever.security.dto.response.UserBindRoleRes;
import org.clever.security.dto.response.UserBindSysRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-03 21:24 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class ManageBySecurityService {

    @Transactional
    public List<UserBindSysRes> userBindSys(UserBindSysReq userBindSysReq) {
        // TODO 用户系统绑定
        return null;
    }

    @Transactional
    public List<UserBindRoleRes> userBindRole(UserBindRoleReq userBindSysReq) {
        // TODO 用户系统绑定
        return null;
    }
}
