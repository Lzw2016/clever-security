package org.clever.security.service;

import lombok.extern.slf4j.Slf4j;
import org.clever.security.client.PasswordRecoverySupportClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/12 21:27 <br/>
 */
@Transactional
@Primary
@Service
@Slf4j
public class PasswordRecoverySupportService implements PasswordRecoverySupportClient {
}
