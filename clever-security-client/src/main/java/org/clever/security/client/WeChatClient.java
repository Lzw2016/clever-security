package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.dto.request.WeChatCode2SessionReq;
import org.clever.security.dto.response.WeChatCode2SessionRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:00 <br/>
 */
@FeignClient(name = Constant.ServerName, url = "https://api.weixin.qq.com/")
public interface WeChatClient {
    /**
     * 登录凭证校验
     */
    @GetMapping("/sns/jscode2session")
    @ResponseBody
    WeChatCode2SessionRes code2Session(WeChatCode2SessionReq req);
}

