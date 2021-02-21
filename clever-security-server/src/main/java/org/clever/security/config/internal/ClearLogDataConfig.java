package org.clever.security.config.internal;

import lombok.Data;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/02/20 23:06 <br/>
 */
@Data
public class ClearLogDataConfig {
    /**
     * 保留user_login_log表数据天数(默认保留最近30天的数据，小于等于0表示不清除历史数据)
     */
    private int userLoginLogRetainOfDays = 30;

    /**
     * 保留user_register_log表数据天数(默认保留最近180天的数据，小于等于0表示不清除历史数据)
     */
    private int userRegisterLogRetainOfDays = 180;

    /**
     * 保留jwt_token表数据天数(默认保留最近30天的数据，小于等于0表示不清除历史数据)
     */
    private int jwtTokenRetainOfDays = 30;

    /**
     * 保留validate_code表数据天数(默认保留最近15天的数据，小于等于0表示不清除历史数据)
     */
    private int validateCodeRetainOfDays = 15;

    /**
     * 保留scan_code_login表数据天数(默认保留最近15天的数据，小于等于0表示不清除历史数据)
     */
    private int scanCodeLoginRetainOfDays = 15;

    /**
     * 保留login_failed_count表数据天数(默认保留最近15天的数据，小于等于0表示不清除历史数据)
     */
    private int loginFailedCountRetainOfDays = 15;
}
