package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.admin.DomainQueryReq;
import org.clever.security.dto.request.admin.UserAddReq;
import org.clever.security.dto.request.admin.UserQueryReq;
import org.clever.security.dto.request.admin.UserUpdateReq;
import org.clever.security.dto.response.admin.UserQueryRes;
import org.clever.security.entity.User;
import org.clever.security.mapper.UserMapper;
import org.clever.security.utils.UserNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public IPage<UserQueryRes> pageQuery(UserQueryReq req) {
        req.addOrderFieldMapping("uid", "a.uid");
        req.addOrderFieldMapping("loginName", "a.login_name");
        req.addOrderFieldMapping("telephone", "a.telephone");
        req.addOrderFieldMapping("email", "a.email");
        req.addOrderFieldMapping("expiredTime", "a.expired_time");
        req.addOrderFieldMapping("enabled", "a.enabled");
        req.addOrderFieldMapping("nickname", "a.nickname");
        req.addOrderFieldMapping("avatar", "a.avatar");
        req.addOrderFieldMapping("registerChannel", "a.register_channel");
        req.addOrderFieldMapping("fromSource", "a.from_source");
        req.addOrderFieldMapping("description", "a.description");
        req.addOrderFieldMapping("createAt", "a.create_at");
        req.addOrderFieldMapping("updateAt", "a.update_at");
        req.addOrderFieldMapping("domainName", "c.name");
        if (req.getOrderFields().isEmpty()) {
            req.addOrderField("createAt", DomainQueryReq.DESC);
        }
        return req.result(userMapper.pageQuery(req));
    }

    @Transactional
    public User addUser(UserAddReq req) {
        User user = BeanMapper.mapper(req, User.class);
        user.setUid(UserNameUtils.generateUid());
//        userMapper.insert(user);
//        return userMapper.getByUid(user.getUid());
        return null;
    }

    @Transactional
    public User updateUser(UserUpdateReq req) {
        User user = BeanMapper.mapper(req, User.class);
//        userMapper.updateById(user);
//        return userMapper.getByUid(user.getUid());
        return null;
    }

}
