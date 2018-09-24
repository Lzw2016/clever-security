package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.exception.BusinessException;
import org.clever.common.server.service.BaseService;
import org.clever.security.entity.RememberMeToken;
import org.clever.security.mapper.RememberMeTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-09-24 16:35 <br/>
 */
@Transactional(readOnly = true)
@Service
@Slf4j
public class RememberMeTokenService extends BaseService {

    @Autowired
    private RememberMeTokenMapper rememberMeTokenMapper;

    /**
     * 新增RememberMeToken
     */
    @Transactional
    public RememberMeToken addRememberMeToken(RememberMeToken req) {
        // 校验token序列号是否唯一
        RememberMeToken exists = rememberMeTokenMapper.getBySeries(req.getSeries());
        if (exists != null) {
            throw new BusinessException("token序列号已经存在");
        }
        rememberMeTokenMapper.insert(req);
        return rememberMeTokenMapper.selectById(req.getId());
    }

    public RememberMeToken getRememberMeToken(String series) {
        return rememberMeTokenMapper.getBySeries(series);
    }

    @Transactional
    public Integer delRememberMeToken(String username) {
        return rememberMeTokenMapper.deleteByUsername(username);
    }

    /**
     * @param series     token序列号
     * @param tokenValue token值
     * @param lastUsed   最后使用时间
     */
    @Transactional
    public RememberMeToken updateRememberMeToken(String series, String tokenValue, Date lastUsed) {
        rememberMeTokenMapper.updateBySeries(series, tokenValue, lastUsed);
        return rememberMeTokenMapper.getBySeries(series);
    }
}
