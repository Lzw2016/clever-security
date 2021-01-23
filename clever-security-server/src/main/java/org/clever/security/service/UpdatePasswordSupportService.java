package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
import org.clever.common.utils.tuples.TupleFour;
import org.clever.security.client.UpdatePasswordSupportClient;
import org.clever.security.crypto.PasswordEncoder;
import org.clever.security.dto.request.InitPasswordReq;
import org.clever.security.dto.request.UpdatePasswordReq;
import org.clever.security.dto.response.InitPasswordRes;
import org.clever.security.dto.response.UpdatePasswordRes;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.User;
import org.clever.security.entity.ValidateCode;
import org.clever.security.mapper.JwtTokenMapper;
import org.clever.security.mapper.UserMapper;
import org.clever.security.mapper.UserSecurityContextMapper;
import org.clever.security.mapper.ValidateCodeMapper;
import org.clever.security.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/18 20:17 <br/>
 */
@Transactional
@Primary
@Service
@Slf4j
public class UpdatePasswordSupportService implements UpdatePasswordSupportClient {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ValidateCodeMapper validateCodeMapper;
    @Autowired
    private JwtTokenMapper jwtTokenMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserSecurityContextMapper userSecurityContextMapper;

    @Override
    public InitPasswordRes initPassword(InitPasswordReq req) {
        User user = userMapper.getByUid(req.getUid());
        if (user == null || StringUtils.isNotBlank(user.getPassword())) {
            throw new BusinessException("已设置密码,无法再次设置");
        }
        User update = new User();
        update.setUid(user.getUid());
        update.setPassword(passwordEncoder.encode(req.getInitPassword()));
        updatePassword(update);
        InitPasswordRes res = new InitPasswordRes();
        res.setSuccess(true);
        return res;
    }

    @Override
    public UpdatePasswordRes updatePassword(UpdatePasswordReq req) {
        User user = userMapper.getByUid(req.getUid());
        if (user == null || !passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            throw new BusinessException("密码不正确");
        }
        User update = new User();
        update.setUid(user.getUid());
        update.setPassword(passwordEncoder.encode(req.getNewPassword()));
        updatePassword(update);
        UpdatePasswordRes res = new UpdatePasswordRes();
        res.setSuccess(true);
        return res;
    }

    /**
     * 生成图片验证码
     *
     * @return {@code TupleFour<Code, Digest, image, ExpiredTime>}
     */
    protected TupleFour<String, String, String, Date> getCaptcha(Long domainId, Integer effectiveTimeMilli) {
        final Date now = new Date();
        ValidateCode validateCode = ValidateCodeUtils.newCaptchaValidateCode(now, domainId, effectiveTimeMilli);
        validateCode.setType(EnumConstant.ValidateCode_Type_13);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_0);
        byte[] image = ImageValidateCageUtils.createImage(validateCode.getCode());
        validateCodeMapper.insert(validateCode);
        return TupleFour.creat(validateCode.getCode(), validateCode.getDigest(), EncodeDecodeUtils.encodeBase64(image), validateCode.getExpiredTime());
    }

    protected void updatePassword(User update) {
        userMapper.updateById(update);
        // 禁用当前用户的Token
        final String disableReason = "用户更新密码";
        jwtTokenMapper.disableJwtTokenByUid(update.getUid(), disableReason);
        // 清除 user_security_context
        userSecurityContextMapper.deleteByUid(update.getUid());
    }
}
