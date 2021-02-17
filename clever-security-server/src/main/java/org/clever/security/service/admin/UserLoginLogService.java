package org.clever.security.service.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.UserLoginLogQueryReq;
import org.clever.security.dto.request.admin.UserQueryReq;
import org.clever.security.dto.response.admin.UserLoginLogQueryRes;
import org.clever.security.entity.UserLoginLog;
import org.clever.security.mapper.UserLoginLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class UserLoginLogService {
    @Autowired
    private UserLoginLogMapper userLoginLogMapper;

    public IPage<UserLoginLogQueryRes> pageQuery(UserLoginLogQueryReq req) {
        return req.result(userLoginLogMapper.pageQuery(req));
    }
}
