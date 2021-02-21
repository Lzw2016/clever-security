package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.MenuPermissionQueryReq;
import org.clever.security.dto.request.admin.UiPermissionQueryReq;
import org.clever.security.dto.response.admin.MenuPermissionQueryRes;
import org.clever.security.dto.response.admin.UiPermissionQueryRes;
import org.clever.security.entity.MenuPermission;
import org.clever.security.mapper.MenuPermissionMapper;
import org.clever.security.mapper.UiPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：ymx <br/>
 * 创建时间：2021/02/16 14:42 <br/>
 */
@Transactional(readOnly = true)
@Service
public class MenuPermissionService {
    @Autowired
    private MenuPermissionMapper menuPermissionMapper;

    public IPage<MenuPermissionQueryRes> pageQuery(MenuPermissionQueryReq req) {
        return req.result(menuPermissionMapper.pageQuery(req));
    }
}
