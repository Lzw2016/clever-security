package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.tuples.TupleThree;
import org.clever.security.entity.EnumConstant;
import org.clever.security.entity.ValidateCode;
import org.clever.security.mapper.ValidateCodeMapper;
import org.clever.security.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/21 21:12 <br/>
 */
@Transactional
@Service
@Slf4j
public class VerifyValidateCodeService {
    @Autowired
    private ValidateCodeMapper validateCodeMapper;

    public TupleThree<Boolean, String, Boolean> verifyValidateCode(long domainId, int type, String captcha, String captchaDigest) {
        return verifyValidateCode(domainId, type, EnumConstant.ValidateCode_SendChannel_0, captcha, captchaDigest, null);
    }

    public TupleThree<Boolean, String, Boolean> verifyValidateCode(long domainId, int type, int sendChannel, String captcha, String captchaDigest, String sendTarget) {
        final Date now = new Date();
        ValidateCode validateCode = validateCodeMapper.getByDigest(domainId, captchaDigest);
        TupleThree<Boolean, String, Boolean> tuple;
        if (Objects.equals(EnumConstant.ValidateCode_SendChannel_0, sendChannel)) {
            tuple = ValidateCodeUtils.verifyValidateCode(validateCode, now, captcha, type);
        } else {
            tuple = ValidateCodeUtils.verifyValidateCode(validateCode, now, captcha, type, sendChannel, sendTarget);
        }
        // 更新验证码
        if (validateCode != null && validateCode.getValidateTime() == null) {
            ValidateCode update = new ValidateCode();
            update.setId(validateCode.getId());
            update.setValidateTime(now);
            validateCodeMapper.updateById(update);
        }
        return tuple;
    }
}
