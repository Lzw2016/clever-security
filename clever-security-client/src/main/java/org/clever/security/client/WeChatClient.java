package org.clever.security.client;

import org.clever.security.Constant;
import org.clever.security.dto.request.WeChatCode2SessionReq;
import org.clever.security.dto.response.WeChatCode2SessionRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/12/13 21:00 <br/>
 */
@FeignClient(name = Constant.ServerName, url = "https://api.weixin.qq.com/")
public interface WeChatClient {

    Map<Integer, String> Code2SessionErrMsgMap = Collections.unmodifiableMap(
            new HashMap<Integer, String>() {{
                put(-1, "系统繁忙，此时请开发者稍候再试");
                put(0, "请求成功");
                put(40029, "code 无效");
                put(45011, "频率限制，每个用户每分钟100次");
            }}
    );

    /**
     * 登录凭证校验
     */
    @GetMapping("/sns/jscode2session")
    @ResponseBody
    WeChatCode2SessionRes code2Session(WeChatCode2SessionReq req);
}

