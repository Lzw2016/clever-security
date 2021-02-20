package org.clever.security.task;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.config.SecurityServerConfig;
import org.clever.security.config.internal.ClearLogDataConfig;
import org.clever.security.service.admin.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时清理历史日志数据
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2021/02/20 22:48 <br/>
 */
@Component
@Slf4j
public class ClearLogDataTask {

    @Autowired
    private SecurityServerConfig securityServerConfig;
    @Autowired
    private UserLoginLogService userLoginLogService;

    @Scheduled(initialDelay = 0, fixedDelay = 1000 * 60 * 60 * 8)
    public void clearLogData() {
        ClearLogDataConfig clearLogDataConfig = securityServerConfig.getClearLogDataConfig();
        long startTime = System.currentTimeMillis();
        log.info("[定时清理历史日志数据] - 开始...");
        if (clearLogDataConfig.getUserLoginLogRetainOfDays() > 0) {
            try {
                int count = userLoginLogService.clearLogData(clearLogDataConfig.getUserLoginLogRetainOfDays());
                log.info("[定时清理历史日志数据] - 删除UserLoginLog数据成功，数量: {}", count);
            } catch (Exception e) {
                log.warn("[定时清理历史日志数据] - 删除UserLoginLog数据失败", e);
            }
        }
        if (clearLogDataConfig.getUserRegisterLogRetainOfDays() > 0) {

        }
        if (clearLogDataConfig.getJwtTokenRetainOfDays() > 0) {

        }
        if (clearLogDataConfig.getValidateCodeRetainOfDays() > 0) {

        }
        if (clearLogDataConfig.getScanCodeLoginRetainOfDays() > 0) {

        }
        if (clearLogDataConfig.getLoginFailedCountRetainOfDays() > 0) {

        }
        long useTime = System.currentTimeMillis() - startTime;
        log.info("[定时清理历史日志数据] - 完成，耗时: {}ms", useTime);
    }
}

// jwt_token 定时刷新状态
// validate_code 定时刷新状态
// scan_code_login 定时刷新状态

