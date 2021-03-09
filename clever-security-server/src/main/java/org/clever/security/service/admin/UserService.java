package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.dto.request.admin.UserAddReq;
import org.clever.security.dto.request.admin.UserQueryReq;
import org.clever.security.dto.request.admin.UserUpdateReq;
import org.clever.security.entity.User;
import org.clever.security.mapper.UserMapper;
import org.clever.security.utils.UserNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public IPage<User> pageQuery(UserQueryReq req) {

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
