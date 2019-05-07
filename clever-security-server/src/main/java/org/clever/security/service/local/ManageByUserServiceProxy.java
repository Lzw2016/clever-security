package org.clever.security.service.local;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.security.client.ManageByUserClient;
import org.clever.security.dto.request.UserAddReq;
import org.clever.security.dto.response.UserAddRes;
import org.clever.security.entity.User;
import org.clever.security.service.ManageByUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-04-29 19:08 <br/>
 */
@Component
@Slf4j
public class ManageByUserServiceProxy implements ManageByUserClient {

    @Autowired
    private ManageByUserService manageByUserService;

    @Override
    public UserAddRes addUser(UserAddReq userAddReq) {
        User user = manageByUserService.addUser(userAddReq);
        return BeanMapper.mapper(user, UserAddRes.class);
    }
}
