package org.clever.security.task;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.service.ServerAccessSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/23 17:43 <br/>
 */
@Component
@Slf4j
public class ReloadServerAccessTokenTask {
    @Autowired
    private ServerAccessSupportService serverAccessSupportService;

    @Scheduled(fixedDelay = 1000 * 8)
    public void reloadServerAccessToken() {
        long startTime = System.currentTimeMillis();
        log.debug("[刷新ServerAccessToken] - 开始...");
        try {
            serverAccessSupportService.reloadServerAccessToken();
        } catch (Exception e) {
            log.debug("[刷新ServerAccessToken] - 失败", e);
        } finally {
            long useTime = System.currentTimeMillis() - startTime;
            log.debug("[刷新ServerAccessToken] - 完成，耗时: {}ms", useTime);
        }
    }
}
