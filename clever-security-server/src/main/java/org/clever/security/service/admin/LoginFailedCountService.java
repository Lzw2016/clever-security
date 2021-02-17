package org.clever.security.service.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.LoginFailedCountQueryReq;
import org.clever.security.dto.response.admin.LoginFailedCountQueryRes;
import org.clever.security.dto.response.admin.UserLoginLogQueryRes;
import org.clever.security.entity.LoginFailedCount;
import org.clever.security.mapper.LoginFailedCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class LoginFailedCountService {
    @Autowired
    private LoginFailedCountMapper loginFailedCountMapper;

    public IPage<LoginFailedCountQueryRes> pageQuery(LoginFailedCountQueryReq req) {
        return req.result(loginFailedCountMapper.pageQuery(req));
    }
}
