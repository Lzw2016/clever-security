package org.clever.security.task;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.service.admin.JwtTokenService;
import org.clever.security.service.admin.ScanCodeLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 更新临时数据状态
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2021/02/22 20:28 <br/>
 */
@Component
@Slf4j
public class RefreshStateTask {
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private ScanCodeLoginService scanCodeLoginService;

    @Scheduled(initialDelay = 0, fixedDelay = 1000 * 60)
    public void refreshState() {
        long startTime = System.currentTimeMillis();
        log.info("[更新临时数据状态] - 开始...");
        try {
            int count = jwtTokenService.refreshState();
            log.info("[更新临时数据状态] - 更新JwtToken状态成功，数量: {}", count);
        } catch (Exception e) {
            log.warn("[更新临时数据状态] - 更新JwtToken状态失败", e);
        }
        try {
            int count = scanCodeLoginService.refreshState();
            log.info("[更新临时数据状态] - 更新ScanCodeLogin状态成功，数量: {}", count);
        } catch (Exception e) {
            log.warn("[更新临时数据状态] - 更新ScanCodeLogin状态失败", e);
        }
        long useTime = System.currentTimeMillis() - startTime;
        log.debug("[更新临时数据状态] - 完成，耗时: {}ms", useTime);
    }
}
