package org.clever.security.service.internal;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.mapper.UserMapper;
import org.clever.security.service.ISessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 操作用户系统绑定的Service
 * <p>
 * 作者： lzw<br/>
 * 创建时间：2018-10-11 9:44 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class UserBindSysNameService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ISessionService sessionService;

    /**
     * 重置用户系统绑定
     *
     * @param userName    用户登录名
     * @param sysNameList 系统名称集合
     */
    @Transactional
    public void resetUserBindSys(String userName, Collection<String> sysNameList) {
        if (sysNameList == null) {
            sysNameList = new ArrayList<>();
        }
        // 获取关联系统列表
        List<String> oldSysNameList = userMapper.findSysNameByUsername(userName);
        Set<String> addSysName = new HashSet<>(sysNameList);
        addSysName.removeAll(oldSysNameList);
        Set<String> delSysName = new HashSet<>(oldSysNameList);
        delSysName.removeAll(sysNameList);
        // 新增
        for (String sysName : addSysName) {
            userMapper.addUserSys(userName, sysName);
        }
        // 删除
        for (String sysName : delSysName) {
            userMapper.delUserSys(userName, sysName);
            // 删除Session
            sessionService.delSession(sysName, userName);
        }
    }
}
