package org.clever.security.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.dto.request.WeChatCode2SessionReq;
import org.clever.security.dto.response.WeChatCode2SessionRes;
import org.clever.security.third.client.WeChatClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/27 14:32 <br/>
 */
@Primary
@Component
@Slf4j
public class MockWeChatClient implements WeChatClient {
    @Override
    public WeChatCode2SessionRes code2Session(WeChatCode2SessionReq req) {
        if (!Objects.equals(req.getJsCode(), "123456")) {
            WeChatCode2SessionRes res = new WeChatCode2SessionRes();
            res.setErrCode(40029);
            res.setErrMsg("code 无效");
            return res;
        }
        WeChatCode2SessionRes res = new WeChatCode2SessionRes();
        res.setOpenId("WeChat123456789");
        res.setSessionKey("abc");
        res.setUnionId("WeChat123456789U");
        res.setErrCode(0);
        res.setErrMsg("");
        return res;
    }
}
