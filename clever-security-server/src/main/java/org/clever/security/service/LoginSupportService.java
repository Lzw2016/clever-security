package org.clever.security.service;

import com.google.zxing.BarcodeFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.exception.BusinessException;
import org.clever.common.utils.DateTimeUtils;
import org.clever.common.utils.IDCreateUtils;
import org.clever.common.utils.SnowFlake;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.imgvalidate.ImageValidateCageUtils;
import org.clever.common.utils.mapper.BeanMapper;
import org.clever.common.utils.tuples.TupleTow;
import org.clever.common.utils.zxing.ZxingCreateImageUtils;
import org.clever.security.client.LoginSupportClient;
import org.clever.security.dto.request.*;
import org.clever.security.dto.response.*;
import org.clever.security.entity.*;
import org.clever.security.mapper.*;
import org.clever.security.model.UserInfo;
import org.clever.security.utils.ConvertUtils;
import org.clever.security.utils.UserNameUtils;
import org.clever.security.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/18 22:23 <br/>
 */
@Transactional
@Primary
@Service
@Slf4j
public class LoginSupportService implements LoginSupportClient {
    @Autowired
    private DomainMapper domainMapper;
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
    @Autowired
    private UserLoginLogMapper userLoginLogMapper;
    @Autowired
    private SendValidateCodeService sendValidateCodeService;

    @Override
    public Domain getDomain(@Validated GetDomainReq req) {
        return domainMapper.selectById(req.getDomainId());
    }

    @Override
    public GetLoginCaptchaRes getLoginCaptcha(GetLoginCaptchaReq req) {
        final Date now = new Date();
        ValidateCode validateCode = ValidateCodeUtils.newCaptchaValidateCode(now, req.getDomainId(), req.getEffectiveTimeMilli());
        validateCode.setType(EnumConstant.ValidateCode_Type_1);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_0);
        byte[] image = ImageValidateCageUtils.createImage(validateCode.getCode());
        validateCodeMapper.insert(validateCode);
        GetLoginCaptchaRes res = new GetLoginCaptchaRes();
        res.setCode(validateCode.getCode());
        res.setDigest(validateCode.getDigest());
        res.setCodeContent(EncodeDecodeUtils.encodeBase64(image));
        res.setExpiredTime(validateCode.getExpiredTime());
        return res;
    }

    @Override
    public VerifyLoginCaptchaRes verifyLoginCaptcha(VerifyLoginCaptchaReq req) {
        final Date now = new Date();
        ValidateCode validateCode = getAndExpiredValidateCode(req.getDomainId(), req.getCaptchaDigest(), now);
        // 验证码验证逻辑
        VerifyLoginCaptchaRes res = new VerifyLoginCaptchaRes();
        if (req.getNeedCaptchaByLoginFailedCount() == null || req.getNeedCaptchaByLoginFailedCount() <= 0) {
            res.setSuccess(true);
            return res;
        }
        User user = getUser(req.getLoginTypeId(), req.getLoginUniqueName());
        LoginFailedCount loginFailedCount = null;
        if (user != null) {
            loginFailedCount = loginFailedCountMapper.getByUidAndLoginType(req.getDomainId(), user.getUid(), req.getLoginTypeId());
        }
        if (loginFailedCount == null || loginFailedCount.getFailedCount() < req.getNeedCaptchaByLoginFailedCount()) {
            res.setSuccess(true);
            return res;
        }
        TupleTow<Boolean, String> tupleTow = ValidateCodeUtils.verifyValidateCode(validateCode, now, req.getCaptcha(), EnumConstant.ValidateCode_Type_1);
        res.setSuccess(tupleTow.getValue1());
        res.setMessage(tupleTow.getValue2());
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
        // 上一次邮箱验证码有效就直接返回
        ValidateCode lastEffective = validateCodeMapper.getLastEffective(
                req.getDomainId(),
                user.getUid(),
                EnumConstant.ValidateCode_Type_1,
                EnumConstant.ValidateCode_SendChannel_2,
                user.getEmail()
        );
        if (lastEffective != null) {
            // 返回
            return ConvertUtils.convertToSendLoginValidateCodeForEmailRes(lastEffective);
        }
        int sendCount = validateCodeMapper.getSendCount(
                req.getDomainId(),
                user.getUid(),
                EnumConstant.ValidateCode_Type_1,
                EnumConstant.ValidateCode_SendChannel_2,
                dayStart,
                dayEnd
        );
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("邮件验证码发送次数超限");
        }
        // 发送邮件验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newEmailValidateCode(now, req.getDomainId(), user.getUid(), user.getEmail(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendEmail(EnumConstant.ValidateCode_Type_1, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_1);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_2);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendLoginValidateCodeForEmailRes(validateCode);
    }

    @Override
    public VerifyLoginEmailValidateCodeRes verifyLoginEmailValidateCode(GetLoginEmailValidateCodeReq req) {
        final Date now = new Date();
        ValidateCode validateCode = getAndExpiredValidateCode(req.getDomainId(), req.getValidateCodeDigest(), now);
        VerifyLoginEmailValidateCodeRes res = new VerifyLoginEmailValidateCodeRes();
        User user = userMapper.getByEmail(req.getEmail());
        if (user == null) {
            res.setSuccess(false);
            res.setMessage("验证码错误");
            return res;
        }
        // 验证码验证逻辑
        TupleTow<Boolean, String> tupleTow = ValidateCodeUtils.verifyValidateCode(validateCode, now, req.getValidateCode(), EnumConstant.ValidateCode_Type_1, EnumConstant.ValidateCode_SendChannel_2, req.getEmail());
        res.setSuccess(tupleTow.getValue1());
        res.setMessage(tupleTow.getValue2());
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
        // 上一次短信验证码有效就直接返回
        ValidateCode lastEffective = validateCodeMapper.getLastEffective(
                req.getDomainId(),
                user.getUid(),
                EnumConstant.ValidateCode_Type_1,
                EnumConstant.ValidateCode_SendChannel_1,
                user.getTelephone()
        );
        if (lastEffective != null) {
            // 返回
            return ConvertUtils.convertToSendLoginValidateCodeForSmsRes(lastEffective);
        }
        int sendCount = validateCodeMapper.getSendCount(
                req.getDomainId(),
                user.getUid(),
                EnumConstant.ValidateCode_Type_1,
                EnumConstant.ValidateCode_SendChannel_1,
                dayStart,
                dayEnd
        );
        if (req.getMaxSendNumInDay() > 0 && sendCount >= req.getMaxSendNumInDay()) {
            throw new BusinessException("短信验证码发送次数超限");
        }
        // 发送短信验证码 - 可多线程异步发送
        ValidateCode validateCode = ValidateCodeUtils.newSmsValidateCode(now, req.getDomainId(), user.getUid(), user.getTelephone(), req.getEffectiveTimeMilli());
        sendValidateCodeService.sendSms(EnumConstant.ValidateCode_Type_1, validateCode.getSendTarget(), validateCode.getCode());
        // 验证码数据写入数据库
        validateCode.setType(EnumConstant.ValidateCode_Type_1);
        validateCode.setSendChannel(EnumConstant.ValidateCode_SendChannel_1);
        validateCodeMapper.insert(validateCode);
        // 返回
        return ConvertUtils.convertToSendLoginValidateCodeForSmsRes(validateCode);
    }

    @Override
    public VerifyLoginSmsValidateCodeRes verifyLoginSmsValidateCode(VerifyLoginSmsValidateCodeReq req) {
        final Date now = new Date();
        ValidateCode validateCode = getAndExpiredValidateCode(req.getDomainId(), req.getValidateCodeDigest(), now);
        VerifyLoginSmsValidateCodeRes res = new VerifyLoginSmsValidateCodeRes();
        User user = userMapper.getByTelephone(req.getTelephone());
        if (user == null) {
            res.setSuccess(false);
            res.setMessage("验证码错误");
            return res;
        }
        // 验证码验证逻辑
        TupleTow<Boolean, String> tupleTow = ValidateCodeUtils.verifyValidateCode(validateCode, now, req.getValidateCode(), EnumConstant.ValidateCode_Type_1, EnumConstant.ValidateCode_SendChannel_1, req.getTelephone());
        res.setSuccess(tupleTow.getValue1());
        res.setMessage(tupleTow.getValue2());
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
        scanCodeLogin.setExpiredTime(new Date(now.getTime() + req.getExpiredTime()));
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
        update.setConfirmExpiredTime(new Date(now.getTime() + req.getConfirmExpiredTime()));
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
        if (scanCodeLogin == null || !Objects.equals(scanCodeLogin.getBindTokenId(), req.getBindTokenId())) {
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
        update.setGetTokenExpiredTime(new Date(now.getTime() + req.getGetTokenExpiredTime()));
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
        if (Objects.equals(scanCodeLogin.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_0)
                || Objects.equals(scanCodeLogin.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_1)
                || Objects.equals(scanCodeLogin.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_2)) {
            final Date now = new Date();
            if ((scanCodeLogin.getExpiredTime() != null && now.compareTo(scanCodeLogin.getExpiredTime()) >= 0)
                    || (scanCodeLogin.getConfirmExpiredTime() != null && now.compareTo(scanCodeLogin.getConfirmExpiredTime()) >= 0)
                    || (scanCodeLogin.getGetTokenExpiredTime() != null && now.compareTo(scanCodeLogin.getGetTokenExpiredTime()) >= 0)) {
                // ScanCodeLogin 已过期失效
                ScanCodeLogin update = new ScanCodeLogin();
                update.setId(scanCodeLogin.getId());
                update.setScanCodeState(EnumConstant.ScanCodeLogin_ScanCodeState_4);
                update.setInvalidReason("过期失效");
                scanCodeLoginMapper.updateById(update);
                scanCodeLogin = scanCodeLoginMapper.getByScanCode(req.getDomainId(), req.getScanCode());
            }
        }
        GetScanCodeLoginInfoRes res = new GetScanCodeLoginInfoRes();
        res.setScanCode(scanCodeLogin.getScanCode());
        res.setScanCodeState(scanCodeLogin.getScanCodeState());
        res.setBindTokenId(scanCodeLogin.getBindTokenId());
        res.setGetTokenExpiredTime(scanCodeLogin.getGetTokenExpiredTime());
        return res;
    }

    @Override
    public ScanCodeLogin writeBackScanCodeLogin(WriteBackScanCodeLoginReq req) {
        ScanCodeLogin scanCodeLogin = scanCodeLoginMapper.getByScanCode(req.getDomainId(), req.getScanCode());
        if (scanCodeLogin == null
                || Objects.equals(scanCodeLogin.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_3)
                || Objects.equals(scanCodeLogin.getScanCodeState(), EnumConstant.ScanCodeLogin_ScanCodeState_4)) {
            return null;
        }
        ScanCodeLogin update = BeanMapper.mapper(req, ScanCodeLogin.class);
        update.setId(scanCodeLogin.getId());
        scanCodeLoginMapper.updateById(update);
        return scanCodeLoginMapper.selectById(update.getId());
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
        String uid;
        if (userExt == null) {
            // TODO 注册用户 UserRegisterLogMapper
            // 微信未绑定用户信息则注册新用户
            uid = UserNameUtils.generateUid();
            User addUser = new User();
            addUser.setUid(uid);
            addUser.setLoginName(UserNameUtils.generateLoginName());
            addUser.setEnabled(EnumConstant.User_Enabled_1);
            addUser.setNickname(addUser.getLoginName());
            addUser.setRegisterChannel(EnumConstant.User_RegisterChannel_5);
            addUser.setFromSource(EnumConstant.User_FromSource_0);
            userMapper.insert(addUser);
            UserDomain addUserDomain = new UserDomain();
            addUserDomain.setDomainId(req.getDomainId());
            addUserDomain.setUid(uid);
            userDomainMapper.insert(addUserDomain);
            UserExt addUserExt = new UserExt();
            addUserExt.setDomainId(req.getDomainId());
            addUserExt.setUid(uid);
            addUserExt.setWechatOpenId(req.getOpenId());
            addUserExt.setWechatUnionId(req.getUnionId());
            userExtMapper.insert(addUserExt);
        } else {
            uid = userExt.getUid();
        }
        User user = userMapper.getByUid(uid);
        return ConvertUtils.convertToUserInfo(user);
    }

    @Override
    public UserInfo getUserInfoByScanCode(GetUserInfoByScanCodeReq req) {
        ScanCodeLogin scanCodeLogin = scanCodeLoginMapper.getByScanCode(req.getDomainId(), req.getScanCode());
        if (scanCodeLogin == null || scanCodeLogin.getBindTokenId() == null) {
            return null;
        }
        JwtToken jwtToken = jwtTokenMapper.selectById(scanCodeLogin.getBindTokenId());
        if (jwtToken == null) {
            return null;
        }
        User user = userMapper.getByUid(jwtToken.getUid());
        return ConvertUtils.convertToUserInfo(user);
    }

    @Override
    public AddUserLoginLogRes addUserLoginLog(AddUserLoginLogReq req) {
        if (StringUtils.isBlank(req.getUid()) && StringUtils.isBlank(req.getLoginUniqueName())) {
            return null;
        }
        if (StringUtils.isBlank(req.getUid())) {
            User user = getUser(req.getLoginType(), req.getLoginUniqueName());
            if (user == null) {
                return null;
            }
            req.setUid(user.getUid());
        }
        UserLoginLog userLoginLog = BeanMapper.mapper(req, UserLoginLog.class);
        userLoginLog.setId(SnowFlake.SNOW_FLAKE.nextId());
        userLoginLogMapper.insert(userLoginLog);
        userLoginLog = userLoginLogMapper.selectById(userLoginLog.getId());
        return BeanMapper.mapper(userLoginLog, AddUserLoginLogRes.class);
    }

    @Override
    public AddLoginFailedCountRes addLoginFailedCount(AddLoginFailedCountReq req) {
        if (StringUtils.isBlank(req.getUid()) && StringUtils.isNotBlank(req.getLoginUniqueName())) {
            User user = getUser(req.getLoginType(), req.getLoginUniqueName());
            if (user == null) {
                return null;
            }
            req.setUid(user.getUid());
        }
        if (StringUtils.isBlank(req.getUid())) {
            return null;
        }
        final Date now = new Date();
        LoginFailedCount loginFailedCount = loginFailedCountMapper.getByUidAndLoginType(req.getDomainId(), req.getUid(), req.getLoginType());
        if (loginFailedCount == null) {
            // 新增数据
            loginFailedCount = new LoginFailedCount();
            loginFailedCount.setId(SnowFlake.SNOW_FLAKE.nextId());
            loginFailedCount.setDomainId(req.getDomainId());
            loginFailedCount.setUid(req.getUid());
            loginFailedCount.setLoginType(req.getLoginType());
            loginFailedCount.setFailedCount(1);
            loginFailedCount.setLastLoginTime(now);
            loginFailedCount.setDeleteFlag(EnumConstant.LoginFailedCount_DeleteFlag_0);
            loginFailedCountMapper.insert(loginFailedCount);
        } else {
            // 更新数据
            loginFailedCountMapper.addLoginFailedCount(req.getDomainId(), req.getUid(), req.getLoginType());
            loginFailedCount.setFailedCount(loginFailedCount.getFailedCount() + 1);
        }
        AddLoginFailedCountRes res = new AddLoginFailedCountRes();
        res.setUid(loginFailedCount.getUid());
        res.setLoginType(loginFailedCount.getLoginType());
        res.setFailedCount(loginFailedCount.getFailedCount());
        res.setLastLoginTime(now);
        return res;
    }

    @Override
    public ClearLoginFailedCountRes clearLoginFailedCount(ClearLoginFailedCountReq req) {
        ClearLoginFailedCountRes res = new ClearLoginFailedCountRes();
        LoginFailedCount loginFailedCount = loginFailedCountMapper.getByUidAndLoginType(req.getDomainId(), req.getUid(), req.getLoginType());
        if (loginFailedCount != null) {
            loginFailedCountMapper.clearLoginFailedCount(req.getDomainId(), req.getUid(), req.getLoginType());
            res.setUid(loginFailedCount.getUid());
            res.setLoginType(loginFailedCount.getLoginType());
            res.setFailedCount(loginFailedCount.getFailedCount());
        }
        return res;
    }

    @Override
    public AddJwtTokenRes addJwtToken(AddJwtTokenReq req) {
        JwtToken jwtToken = BeanMapper.mapper(req, JwtToken.class);
        jwtToken.setDisable(EnumConstant.JwtToken_Disable_0);
        jwtToken.setRefreshTokenState(EnumConstant.JwtToken_RefreshTokenState_1);
        jwtTokenMapper.insert(jwtToken);
        jwtToken = jwtTokenMapper.selectById(jwtToken.getId());
        return BeanMapper.mapper(jwtToken, AddJwtTokenRes.class);
    }

    @Override
    public DisableFirstJwtTokenRes disableFirstJwtToken(DisableFirstJwtTokenReq req) {
        List<JwtToken> jwtTokenList = jwtTokenMapper.getFirstJwtToken(req.getDomainId(), req.getUid(), req.getDisableCount());
        if (jwtTokenList == null || jwtTokenList.isEmpty()) {
            return null;
        }
        int disableCount = 0;
        for (JwtToken jwtToken : jwtTokenList) {
            JwtToken update = new JwtToken();
            update.setId(jwtToken.getId());
            update.setDisable(EnumConstant.JwtToken_Disable_1);
            update.setDisableReason(req.getDisableReason());
            disableCount += jwtTokenMapper.updateById(update);
        }
        DisableFirstJwtTokenRes res = new DisableFirstJwtTokenRes();
        res.setUid(req.getUid());
        res.setDisableCount(disableCount);
        return res;
    }

    @Override
    public JwtToken getJwtToken(GetJwtTokenReq req) {
        JwtToken jwtToken = jwtTokenMapper.selectById(req.getId());
        if (!Objects.equals(req.getDomainId(), jwtToken.getDomainId())) {
            return null;
        }
        return jwtToken;
    }

    @Override
    public JwtToken disableJwtToken(DisableJwtTokenReq req) {
        int count = jwtTokenMapper.disableJwtToken(req.getDomainId(), req.getId(), req.getDisableReason());
        if (count <= 0) {
            return null;
        }
        return jwtTokenMapper.selectById(req.getId());
    }

    @Override
    public JwtToken useJwtRefreshToken(UseJwtRefreshTokenReq req) {
        final Date now = new Date();
        JwtToken useToken = jwtTokenMapper.selectById(req.getUseJwtId());
        // 验证当前Token，异常场景 --> 1.不存在，2.未过期，3.被禁用，4.刷新Token值为空，5.刷新Token内容不一致，6.刷新Token已过期，7.刷新Token无效
        if (useToken == null
                || (useToken.getExpiredTime() != null && now.compareTo(useToken.getExpiredTime()) < 0)
                || !Objects.equals(useToken.getDisable(), EnumConstant.JwtToken_Disable_0)
                || StringUtils.isBlank(useToken.getRefreshToken())
                || !Objects.equals(req.getUseRefreshToken(), useToken.getRefreshToken())
                || (useToken.getRefreshTokenExpiredTime() != null && now.compareTo(useToken.getRefreshTokenExpiredTime()) >= 0)
                || !Objects.equals(useToken.getRefreshTokenState(), EnumConstant.JwtToken_RefreshTokenState_1)) {
            return null;
        }
        // 新增Token
        JwtToken add = new JwtToken();
        add.setId(req.getJwtId());
        add.setDomainId(req.getDomainId());
        add.setUid(useToken.getUid());
        add.setToken(req.getToken());
        add.setExpiredTime(req.getExpiredTime());
        add.setDisable(EnumConstant.JwtToken_Disable_0);
        add.setRefreshToken(req.getRefreshToken());
        add.setRefreshTokenExpiredTime(req.getRefreshTokenExpiredTime());
        add.setRefreshTokenState(EnumConstant.JwtToken_RefreshTokenState_1);
        jwtTokenMapper.insert(add);
        // 更新Token
        JwtToken update = new JwtToken();
        update.setId(useToken.getId());
        update.setDisable(EnumConstant.JwtToken_Disable_1);
        update.setDisableReason("使用RefreshToken");
        update.setRefreshTokenState(EnumConstant.JwtToken_RefreshTokenState_0);
        update.setRefreshTokenUseTime(now);
        update.setRefreshCreateTokenId(add.getId());
        jwtTokenMapper.updateById(update);
        // 返回新增的Token
        return jwtTokenMapper.selectById(add.getId());
    }

    protected ValidateCode getAndExpiredValidateCode(Long domainId, String digest, Date now) {
        ValidateCode validateCode = validateCodeMapper.getByDigest(domainId, digest);
        // 设置验证码已经验证
        if (validateCode != null && validateCode.getValidateTime() == null) {
            ValidateCode update = new ValidateCode();
            update.setId(validateCode.getId());
            update.setValidateTime(now);
            validateCodeMapper.updateById(update);
        }
        return validateCode;
    }

    /**
     * 获取用户信息
     *
     * @param loginTypeId     登录方式ID(1:用户名密码，2:手机号验证码，3:邮箱验证码，4:刷新令牌，5:微信小程序，6:扫码登录)
     * @param loginUniqueName 登录唯一名称(查询用户条件) 1.用户名密码   -> loginName | 2.手机号验证码 -> telephone | 3.邮箱验证码   -> email
     */
    protected User getUser(int loginTypeId, String loginUniqueName) {
        User user = null;
        switch (loginTypeId) {
            case EnumConstant.UserLoginLog_LoginType_1:
                user = userMapper.getByLoginName(loginUniqueName);
                break;
            case EnumConstant.UserLoginLog_LoginType_2:
                user = userMapper.getByTelephone(loginUniqueName);
                break;
            case EnumConstant.UserLoginLog_LoginType_3:
                user = userMapper.getByEmail(loginUniqueName);
                break;
        }
        return user;
    }
}
