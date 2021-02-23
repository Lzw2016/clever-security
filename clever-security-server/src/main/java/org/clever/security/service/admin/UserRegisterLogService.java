package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.UserRegisterLogQueryReq;
import org.clever.security.dto.response.admin.UserRegisterLogQueryRes;
import org.clever.security.mapper.UserRegisterLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class UserRegisterLogService {
    @Autowired
    private UserRegisterLogMapper userRegisterLogMapper;

    public IPage<UserRegisterLogQueryRes> pageQuery(UserRegisterLogQueryReq req) {
        return req.result(userRegisterLogMapper.pageQuery(req));
    }

    @Transactional
    public int clearLogData(int retainOfDays) {
        if (retainOfDays <= 0) {
            return 0;
        }
        return userRegisterLogMapper.clearLogData(retainOfDays);
    }
}
