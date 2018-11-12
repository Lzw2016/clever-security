package org.clever.security.job;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.GlobalJob;
import org.springframework.stereotype.Component;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-11-12 12:49 <br/>
 */
@Component
@Slf4j
public class CheckUserLoginLogJob extends GlobalJob {

    @Override
    protected void internalExecute() {
        // TODO 检查登录状态
    }

    @Override
    protected void exceptionHandle(Throwable e) {

    }
}
