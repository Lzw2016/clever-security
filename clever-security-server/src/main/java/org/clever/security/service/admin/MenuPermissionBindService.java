package org.clever.security.service.admin;

import org.clever.security.mapper.MenuPermissionBindMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class MenuPermissionBindService {
    @Autowired
    private MenuPermissionBindMapper menuPermissionBindMapper;

}
