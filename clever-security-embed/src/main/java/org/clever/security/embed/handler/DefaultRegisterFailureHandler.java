package org.clever.security.embed.handler;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.RegisterSupportClient;
import org.clever.security.dto.request.AddUserRegisterLogReq;
import org.clever.security.dto.response.AddUserRegisterLogRes;
import org.clever.security.embed.event.RegisterFailureEvent;
import org.clever.security.embed.utils.HttpServletRequestUtils;
import org.clever.security.entity.EnumConstant;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2021-01-11 21:17 <br/>
 */
@Slf4j
public class DefaultRegisterFailureHandler implements RegisterFailureHandler {
    private final RegisterSupportClient registerSupportClient;

    public DefaultRegisterFailureHandler(RegisterSupportClient registerSupportClient) {
        Assert.notNull(registerSupportClient, "参数registerSupportClient不能为null");
        this.registerSupportClient = registerSupportClient;
    }

    @Override
    public void onRegisterFailure(HttpServletRequest request, HttpServletResponse response, RegisterFailureEvent event) {
        AddUserRegisterLogReq req = new AddUserRegisterLogReq(event.getDomainId());
        req.setRegisterIp(request.getRemoteAddr());
        req.setRegisterChannel(event.getRegisterChannel());
        req.setRegisterType(event.getRegisterType());
        req.setRequestData(HttpServletRequestUtils.getBodyData(request));
        req.setRequestResult(EnumConstant.UserRegisterLog_RequestResult_1);
        req.setFailReason(event.getRegisterException().getMessage());
        AddUserRegisterLogRes res = registerSupportClient.addUserRegisterLog(req);
        log.debug("用户注册失败,注册日志 -> {}", res);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
