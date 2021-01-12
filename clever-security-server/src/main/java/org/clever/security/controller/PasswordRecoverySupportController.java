package org.clever.security.controller;

import org.clever.security.client.PasswordRecoverySupportClient;
import org.clever.security.service.PasswordRecoverySupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/12 21:26 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class PasswordRecoverySupportController implements PasswordRecoverySupportClient {
    @Autowired
    private PasswordRecoverySupportService passwordRecoverySupportService;

}
