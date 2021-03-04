package org.clever.security.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.clever.security.dto.request.admin.UiPermissionAddReq;
import org.clever.security.dto.request.admin.UiPermissionQueryReq;
import org.clever.security.dto.request.admin.UiPermissionUpdateReq;
import org.clever.security.dto.response.admin.UiPermissionQueryRes;
import org.clever.security.entity.Domain;
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
public class UiPermissionService {
    @Autowired
    private UiPermissionMapper uiPermissionMapper;

    public IPage<UiPermissionQueryRes> pageQuery(UiPermissionQueryReq req) {
        return req.result(uiPermissionMapper.pageQuery(req));
    }

    @Transactional
    public Domain addUiPermission(UiPermissionAddReq req) {
        return null;
    }

    @Transactional
    public Domain updateUiPermission(UiPermissionUpdateReq req) {
        return null;
    }

    @Transactional
    public Domain delUiPermission(Long domainId, Long id) {
        return null;
    }
}
