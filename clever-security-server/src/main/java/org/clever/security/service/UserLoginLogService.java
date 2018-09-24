package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.security.entity.UserLoginLog;
import org.clever.security.mapper.UserLoginLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-23 16:03 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class UserLoginLogService {

    @Autowired
    private UserLoginLogMapper userLoginLogMapper;

    @Transactional
    public UserLoginLog addUserLoginLog(UserLoginLog userLoginLog) {
        userLoginLogMapper.insert(userLoginLog);
        return userLoginLogMapper.selectById(userLoginLog.getId());
    }

    public UserLoginLog getUserLoginLog(String sessionId) {
        return userLoginLogMapper.getBySessionId(sessionId);
    }

    public UserLoginLog updateUserLoginLog(String sessionId, UserLoginLog update) {
        UserLoginLog old = userLoginLogMapper.getBySessionId(sessionId);
        if (old == null) {
            throw new BusinessException("登录记录不存在，SessionId=" + sessionId);
        }
        update.setId(old.getId());
        userLoginLogMapper.updateById(update);
        return userLoginLogMapper.selectById(old.getId());
    }
}
