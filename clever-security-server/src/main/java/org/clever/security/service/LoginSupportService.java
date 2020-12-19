package org.clever.security.service;

import com.google.zxing.BarcodeFormat;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.IDCreateUtils;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
import org.clever.common.utils.imgvalidate.ValidateCodeSourceUtils;
import org.clever.common.utils.zxing.ZxingCreateImageUtils;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.*;
import org.clever.security.mapper.*;
import org.clever.security.model.UserInfo;
import org.clever.security.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/18 22:23 <br/>
 */
@Transactional
@Primary
@Service
public class LoginSupportService implements LoginSupportClient {
    @Autowired
    private ValidateCodeMapper validateCodeMapper;
    @Autowired
    private LoginFailedCountMapper loginFailedCountMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ScanCodeLoginMapper scanCodeLoginMapper;
    @Autowired
    private JwtTokenMapper jwtTokenMapper;
    @Autowired
    private UserDomainMapper userDomainMapper;
    @Autowired
    private UserExtMapper userExtMapper;

    @Override
    public GetLoginCaptchaRes getLoginCaptcha(GetLoginCaptchaReq req) {
        String code = ValidateCodeSourceUtils.getRandString(4);
        byte[] image = ImageValidateCageUtils.createImage(code);
        ValidateCode validateCode = new ValidateCode();
        validateCode.setId(SnowFlake.SNOW_FLAKE.nextId());
        validateCode.setDomainId(req.getDomainId());
        validateCode.setCode(code);
        validateCode.setDigest(IDCreateUtils.uuid());
        validateCode.setType(EnumConstant.ValidateCode_Type_1);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_0);
        validateCode.setExpiredTime(DateTimeUtils.addMilliseconds(new Date(), req.getEffectiveTimeMilli()));
        validateCodeMapper.insert(validateCode);
        GetLoginCaptchaRes res = new GetLoginCaptchaRes();
        res.setCode(code);
        res.setDigest(validateCode.getDigest());
        res.setCodeContent(EncodeDecodeUtils.encodeBase64(image));
        res.setExpiredTime(validateCode.getExpiredTime());
        return res;
    }

    @Override
    public GetLoginFailedCountAndCaptchaRes getLoginFailedCountAndCaptcha(GetLoginFailedCountAndCaptchaReq req) {
        final Date now = new Date();
        GetLoginFailedCountAndCaptchaRes res = new GetLoginFailedCountAndCaptchaRes();
        res.setFailedCount(0);
        ValidateCode validateCode = validateCodeMapper.getByDigest(req.getDomainId(), req.getCaptchaDigest());
        if (validateCode != null && validateCode.getValidateTime() == null) {
            res.setCode(validateCode.getCode());
            res.setDigest(validateCode.getDigest());
            res.setExpiredTime(validateCode.getExpiredTime());
        }
        if (validateCode != null) {
            ValidateCode update = new ValidateCode();
            update.setId(validateCode.getId());
            update.setValidateTime(now);
            validateCodeMapper.updateById(update);
        }
        User user = null;
        switch (req.getLoginTypeId()) {
            case EnumConstant.UserLoginLog_LoginType_1:
                user = userMapper.getByLoginName(req.getLoginUniqueName());
                break;
            case EnumConstant.UserLoginLog_LoginType_2:
                user = userMapper.getByTelephone(req.getLoginUniqueName());
                break;
            case EnumConstant.UserLoginLog_LoginType_3:
                user = userMapper.getByEmail(req.getLoginUniqueName());
                break;
        }
        if (user != null) {
            LoginFailedCount loginFailedCount = loginFailedCountMapper.getByUidAndLoginType(req.getDomainId(), user.getUid(), req.getLoginTypeId());
            if (loginFailedCount != null) {
                res.setFailedCount(loginFailedCount.getFailedCount());
                res.setLastLoginTime(loginFailedCount.getLastLoginTime());
            }
        }
        return res;
    }

    @Override
    public SendLoginValidateCodeForEmailRes sendLoginValidateCodeForEmail(SendLoginValidateCodeForEmailReq req) {
        final Date now = new Date();
        final Date dayStart = DateTimeUtils.getDayStartTime(now);
        final Date dayEnd = DateTimeUtils.getDayEndTime(now);
        User user = userMapper.getByEmail(req.getEmail());
        if (user == null) {
            return null;
        }
        int sendCount = validateCodeMapper.getSendCount(
                req.getDomainId(),
                user.getUid(),
                EnumConstant.ValidateCode_Type_1,
                EnumConstant.ValidateCode_SendChannel_2,
                dayStart,
                dayEnd
        );
        if (req.getMaxSendNumInDay() > 0 && sendCount > req.getMaxSendNumInDay()) {
            throw new BusinessException("邮件验证码发送次数超限");
        }
        String code = ValidateCodeSourceUtils.getRandString(6);
        // TODO 发送邮件验证码 - 可多线程异步发送
        byte[] image = ImageValidateCageUtils.createImage(code);
        // 验证码数据写入数据库
        ValidateCode validateCode = new ValidateCode();
        validateCode.setId(SnowFlake.SNOW_FLAKE.nextId());
        validateCode.setDomainId(req.getDomainId());
        validateCode.setUid(user.getUid());
        validateCode.setCode(code);
        validateCode.setDigest(IDCreateUtils.uuid());
        validateCode.setType(EnumConstant.ValidateCode_Type_1);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_2);
        validateCode.setExpiredTime(DateTimeUtils.addMilliseconds(new Date(), req.getEffectiveTimeMilli()));
        validateCodeMapper.insert(validateCode);
        // 返回
        SendLoginValidateCodeForEmailRes res = new SendLoginValidateCodeForEmailRes();
        res.setCode(code);
        res.setDigest(validateCode.getDigest());
        res.setExpiredTime(validateCode.getExpiredTime());
        return res;
    }

    @Override
    public SendLoginValidateCodeForSmsRes sendLoginValidateCodeForSms(SendLoginValidateCodeForSmsReq req) {
        final Date now = new Date();
        final Date dayStart = DateTimeUtils.getDayStartTime(now);
        final Date dayEnd = DateTimeUtils.getDayEndTime(now);
        User user = userMapper.getByTelephone(req.getTelephone());
        if (user == null) {
            return null;
        }
        int sendCount = validateCodeMapper.getSendCount(
                req.getDomainId(),
                user.getUid(),
                EnumConstant.ValidateCode_Type_1,
                EnumConstant.ValidateCode_SendChannel_1,
                dayStart,
                dayEnd
        );
        if (req.getMaxSendNumInDay() > 0 && sendCount > req.getMaxSendNumInDay()) {
            throw new BusinessException("短信验证码发送次数超限");
        }
        String code = ValidateCodeSourceUtils.getRandString(6);
        // TODO 发送短信验证码 - 可多线程异步发送
        byte[] image = ImageValidateCageUtils.createImage(code);
        // 验证码数据写入数据库
        ValidateCode validateCode = new ValidateCode();
        validateCode.setId(SnowFlake.SNOW_FLAKE.nextId());
        validateCode.setDomainId(req.getDomainId());
        validateCode.setUid(user.getUid());
        validateCode.setCode(code);
        validateCode.setDigest(IDCreateUtils.uuid());
        validateCode.setType(EnumConstant.ValidateCode_Type_1);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_1);
        validateCode.setExpiredTime(DateTimeUtils.addMilliseconds(new Date(), req.getEffectiveTimeMilli()));
        validateCodeMapper.insert(validateCode);
        // 返回
        SendLoginValidateCodeForSmsRes res = new SendLoginValidateCodeForSmsRes();
        res.setCode(code);
        res.setDigest(validateCode.getDigest());
        res.setExpiredTime(validateCode.getExpiredTime());
        return res;
    }

    @Override
    public CreateLoginScanCodeRes createLoginScanCode(CreateLoginScanCodeReq req) {
        final Date now = new Date();
        ScanCodeLogin scanCodeLogin = new ScanCodeLogin();
        scanCodeLogin.setId(SnowFlake.SNOW_FLAKE.nextId());
        scanCodeLogin.setDomainId(req.getDomainId());
        scanCodeLogin.setScanCode(IDCreateUtils.shortUuid());
        scanCodeLogin.setScanCodeState(EnumConstant.ScanCodeLogin_ScanCodeState_0);
        scanCodeLogin.setExpiredTime(DateTimeUtils.addMilliseconds(now, req.getExpiredTime()));
        scanCodeLoginMapper.insert(scanCodeLogin);
        // 返回
        byte[] image = ZxingCreateImageUtils.createImage(scanCodeLogin.getScanCode(), BarcodeFormat.QR_CODE);
        CreateLoginScanCodeRes res = new CreateLoginScanCodeRes();
        res.setScanCode(scanCodeLogin.getScanCode());
        res.setScanCodeState(scanCodeLogin.getScanCodeState());
        res.setExpiredTime(scanCodeLogin.getExpiredTime());
        res.setScanCodeContent(EncodeDecodeUtils.encodeBase64(image));
        return res;
    }

    @Override
    public BindLoginScanCodeRes bindLoginScanCode(BindLoginScanCodeReq req) {
        ScanCodeLogin scanCodeLogin = scanCodeLoginMapper.getByScanCode(req.getDomainId(), req.getScanCode());
        if (scanCodeLogin == null) {
            return null;
        }
        if (!Objects.equals(scanCodeLogin.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_0)) {
            return null;
        }
        final Date now = new Date();
        if (now.compareTo(scanCodeLogin.getExpiredTime()) >= 0) {
            return null;
        }
        JwtToken jwtToken = jwtTokenMapper.getEffectiveTokenById(req.getBindTokenId());
        if (jwtToken == null) {
            return null;
        }
        ScanCodeLogin update = new ScanCodeLogin();
        update.setId(scanCodeLogin.getId());
        update.setScanCodeState(EnumConstant.ScanCodeLogin_ScanCodeState_1);
        update.setBindTokenId(jwtToken.getId());
        update.setBindTokenTime(now);
        update.setConfirmExpiredTime(DateTimeUtils.addMilliseconds(now, req.getConfirmExpiredTime()));
        scanCodeLoginMapper.updateById(update);
        // 返回
        BindLoginScanCodeRes res = new BindLoginScanCodeRes();
        res.setUid(jwtToken.getUid());
        res.setBindTokenTime(now);
        res.setConfirmExpiredTime(update.getConfirmExpiredTime());
        return res;
    }

    @Override
    public ConfirmLoginScanCodeRes confirmLoginScanCode(ConfirmLoginScanCodeReq req) {
        ScanCodeLogin scanCodeLogin = scanCodeLoginMapper.getByScanCode(req.getDomainId(), req.getScanCode());
        if (scanCodeLogin == null) {
            return null;
        }
        if (!Objects.equals(scanCodeLogin.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_1)) {
            return null;
        }
        final Date now = new Date();
        if (now.compareTo(scanCodeLogin.getConfirmExpiredTime()) >= 0) {
            return null;
        }
        JwtToken jwtToken = jwtTokenMapper.getEffectiveTokenById(req.getBindTokenId());
        if (jwtToken == null) {
            return null;
        }
        ScanCodeLogin update = new ScanCodeLogin();
        update.setId(scanCodeLogin.getId());
        update.setScanCodeState(EnumConstant.ScanCodeLogin_ScanCodeState_2);
        update.setBindTokenId(jwtToken.getId());
        update.setConfirmTime(now);
        update.setGetTokenExpiredTime(DateTimeUtils.addMilliseconds(now, req.getGetTokenExpiredTime()));
        scanCodeLoginMapper.updateById(update);
        // 返回
        ConfirmLoginScanCodeRes res = new ConfirmLoginScanCodeRes();
        res.setUid(jwtToken.getUid());
        res.setConfirmTime(now);
        res.setGetTokenExpiredTime(update.getGetTokenExpiredTime());
        return res;
    }

    @Override
    public GetScanCodeLoginInfoRes getScanCodeLoginInfo(GetScanCodeLoginInfoReq req) {
        ScanCodeLogin scanCodeLogin = scanCodeLoginMapper.getByScanCode(req.getDomainId(), req.getScanCode());
        if (scanCodeLogin == null) {
            return null;
        }
        GetScanCodeLoginInfoRes res = new GetScanCodeLoginInfoRes();
        res.setScanCode(scanCodeLogin.getScanCode());
        res.setScanCodeState(scanCodeLogin.getScanCodeState());
        res.setBindTokenId(scanCodeLogin.getBindTokenId());
        res.setGetTokenExpiredTime(scanCodeLogin.getGetTokenExpiredTime());
        return res;
    }

    @Override
    public ValidateCode getLoginSmsValidateCode(GetLoginSmsValidateCodeReq req) {
        User user = userMapper.getByTelephone(req.getTelephone());
        if (user == null) {
            return null;
        }
        ValidateCode validateCode = validateCodeMapper.getByDigest(req.getDomainId(), req.getValidateCodeDigest());
        if (validateCode != null) {
            if (!Objects.equals(validateCode.getType(), EnumConstant.ValidateCode_Type_1) || !Objects.equals(validateCode.getSendChannel(), EnumConstant.ValidateCode_SendChannel_1)) {
                return null;
            }
            ValidateCode update = new ValidateCode();
            update.setId(validateCode.getId());
            update.setValidateTime(new Date());
            validateCodeMapper.updateById(update);
        }
        return validateCode;
    }

    @Override
    public ValidateCode getLoginEmailValidateCode(GetLoginEmailValidateCodeReq req) {
        User user = userMapper.getByEmail(req.getEmail());
        if (user == null) {
            return null;
        }
        ValidateCode validateCode = validateCodeMapper.getByDigest(req.getDomainId(), req.getValidateCodeDigest());
        if (validateCode != null) {
            if (!Objects.equals(validateCode.getType(), EnumConstant.ValidateCode_Type_1) || !Objects.equals(validateCode.getSendChannel(), EnumConstant.ValidateCode_SendChannel_2)) {
                return null;
            }
            ValidateCode update = new ValidateCode();
            update.setId(validateCode.getId());
            update.setValidateTime(new Date());
            validateCodeMapper.updateById(update);
        }
        return validateCode;
    }

    @Override
    public DomainExistsUserRes domainExistsUser(DomainExistsUserReq req) {
        int count = userDomainMapper.exists(req.getDomainId(), req.getUid());
        DomainExistsUserRes res = new DomainExistsUserRes();
        res.setExists(count > 0);
        if (res.isExists()) {
            User user = userMapper.getByUid(req.getUid());
            if (user == null) {
                res.setExists(false);
            } else {
                res.setUid(user.getUid());
                res.setLoginName(user.getLoginName());
            }
        }
        return res;
    }

    @Override
    public User getUser(GetUserReq req) {
        int count = userDomainMapper.exists(req.getDomainId(), req.getUid());
        if (count <= 0) {
            return null;
        }
        return userMapper.getByUid(req.getUid());
    }

    @Override
    public GetConcurrentLoginCountRes getConcurrentLoginCount(GetConcurrentLoginCountReq req) {
        int concurrentLoginCount = jwtTokenMapper.getConcurrentLoginCount(req.getDomainId(), req.getUid());
        GetConcurrentLoginCountRes res = new GetConcurrentLoginCountRes();
        res.setUid(req.getUid());
        res.setConcurrentLoginCount(concurrentLoginCount);
        return res;
    }

    @Override
    public UserInfo getUserInfoByLoginName(GetUserInfoByLoginNameReq req) {
        User user = userMapper.getByLoginName(req.getLoginName());
        return ConvertUtils.convertToUserInfo(user);
    }

    @Override
    public UserInfo getUserInfoByTelephone(GetUserInfoByTelephoneReq req) {
        User user = userMapper.getByTelephone(req.getTelephone());
        return ConvertUtils.convertToUserInfo(user);
    }

    @Override
    public UserInfo getUserInfoByEmail(GetUserInfoByEmailReq req) {
        User user = userMapper.getByEmail(req.getEmail());
        return ConvertUtils.convertToUserInfo(user);
    }

    @Override
    public UserInfo getUserInfoByWechatOpenId(GetUserInfoByWechatOpenIdReq req) {
        UserExt userExt = userExtMapper.getByWechatOpenId(req.getDomainId(), req.getOpenId());
        if (userExt == null) {
            return null;
        }
        User user = userMapper.getByUid(userExt.getUid());
        return ConvertUtils.convertToUserInfo(user);
    }

    @Override
    public UserInfo getUserInfoByScanCode(GetUserInfoByScanCodeReq req) {
        ScanCodeLogin scanCodeLogin = scanCodeLoginMapper.getByScanCode(req.getDomainId(), req.getScanCode());
        if (scanCodeLogin == null || scanCodeLogin.getTokenId() == null) {
            return null;
        }
        JwtToken jwtToken = jwtTokenMapper.selectById(scanCodeLogin.getTokenId());
        if (jwtToken == null) {
            return null;
        }
        User user = userMapper.getByUid(jwtToken.getUid());
        return ConvertUtils.convertToUserInfo(user);
    }

    @Override
    public AddUserLoginLogRes addUserLoginLog(AddUserLoginLogReq req) {
        return null;
    }

    @Override
    public AddLoginFailedCountRes addLoginFailedCount(AddLoginFailedCountReq req) {
        return null;
    }

    @Override
    public ClearLoginFailedCountRes clearLoginFailedCount(ClearLoginFailedCountReq req) {
        return null;
    }

    @Override
    public AddJwtTokenRes addJwtToken(AddJwtTokenReq req) {
        return null;
    }

    @Override
    public DisableFirstJwtTokenRes disableFirstJwtToken(DisableFirstJwtTokenReq req) {
        return null;
    }

    @Override
    public JwtToken getJwtToken(GetJwtTokenReq req) {
        return null;
    }

    @Override
    public JwtToken disableJwtToken(DisableJwtTokenReq req) {
        return null;
    }
}
