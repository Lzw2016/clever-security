package org.clever.security.controller;

import org.clever.security.client.BindSupportClient;
import org.clever.security.service.BindSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/01/17 20:46 <br/>
 */
@RestController
@RequestMapping("/security/api")
public class BindSupportController implements BindSupportClient {
    @Autowired
    private BindSupportService bindSupportService;
}
