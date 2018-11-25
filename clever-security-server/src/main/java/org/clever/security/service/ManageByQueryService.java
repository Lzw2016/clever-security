package org.clever.security.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.clever.security.dto.request.RememberMeTokenQueryReq;
import org.clever.security.dto.request.UserLoginLogQueryReq;
import org.clever.security.entity.model.UserLoginLogModel;
import org.clever.security.entity.model.UserRememberMeToken;
import org.clever.security.mapper.QueryMapper;
import org.clever.security.mapper.ServiceSysMapper;
import org.clever.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-07 19:38 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class ManageByQueryService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QueryMapper queryMapper;
    @Autowired
    private ServiceSysMapper serviceSysMapper;

    public Boolean existsUserByUsername(String username) {
        return userMapper.existsByUserName(username) > 0;
    }

    public Boolean existsUserByTelephone(String telephone) {
        return userMapper.existsByTelephone(telephone) > 0;
    }

    public Boolean existsUserByEmail(String email) {
        return userMapper.existsByEmail(email) > 0;
    }

    public List<String> allSysName() {
        List<String> sysNameList = serviceSysMapper.allSysName();
        List<String> tmp = queryMapper.allSysName();
        for (String sysName : tmp) {
            if (!sysNameList.contains(sysName)) {
                sysNameList.add(sysName);
            }
        }
        return sysNameList;
    }

    public List<String> findSysNameByUser(String username) {
        return userMapper.findSysNameByUsername(username);
    }

    public List<String> allRoleName() {
        return queryMapper.allRoleName();
    }

    public List<String> findRoleNameByUser(String username) {
        return queryMapper.findRoleNameByUser(username);
    }

    public List<String> findPermissionStrByRole(String roleName) {
        return queryMapper.findPermissionStrByRole(roleName);
    }

    public IPage<UserRememberMeToken> findRememberMeToken(RememberMeTokenQueryReq req) {
        Page<UserRememberMeToken> page = new Page<>(req.getPageNo(), req.getPageSize());
        page.setRecords(queryMapper.findRememberMeToken(req, page));
        return page;
    }

    public IPage<UserLoginLogModel> findUserLoginLog(UserLoginLogQueryReq req) {
        Page<UserLoginLogModel> page = new Page<>(req.getPageNo(), req.getPageSize());
        page.setRecords(queryMapper.findUserLoginLog(req, page));
        return page;
    }
}
