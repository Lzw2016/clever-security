package org.clever.security.service;

import org.clever.security.client.BindSupportClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/17 20:46 <br/>
 */
@Transactional
@Primary
@Service
public class BindSupportService implements BindSupportClient {
}
