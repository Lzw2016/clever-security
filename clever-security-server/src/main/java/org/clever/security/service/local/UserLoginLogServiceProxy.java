package org.clever.security.service.local;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.client.UserLoginLogClient;
import org.clever.security.dto.request.UserLoginLogAddReq;
import org.clever.security.dto.request.UserLoginLogUpdateReq;
import org.clever.security.entity.UserLoginLog;
import org.clever.security.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 参考 UserLoginLogController
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-11-11 19:30 <br/>
 */
@Component
@Slf4j
public class UserLoginLogServiceProxy implements UserLoginLogClient {
    @Autowired
    private UserLoginLogService userLoginLogService;

    @Override
    public UserLoginLog addUserLoginLog(UserLoginLogAddReq req) {
        UserLoginLog userLoginLog = BeanMapper.mapper(req, UserLoginLog.class);
        return userLoginLogService.addUserLoginLog(userLoginLog);
    }

    @Override
    public UserLoginLog getUserLoginLog(String sessionId) {
        return userLoginLogService.getUserLoginLog(sessionId);
    }

    @Override
    public UserLoginLog updateUserLoginLog(String sessionId, UserLoginLogUpdateReq req) {
        UserLoginLog update = BeanMapper.mapper(req, UserLoginLog.class);
        return userLoginLogService.updateUserLoginLog(sessionId, update);
    }
}
