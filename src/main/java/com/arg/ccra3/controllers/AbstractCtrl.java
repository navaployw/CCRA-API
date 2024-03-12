
package com.arg.ccra3.controllers;
 
import org.springframework.web.bind.annotation.RestController;

import com.arg.ccra3.online.service.CtrlKeyService;
import com.arg.ccra3.online.service.MessageErrorService;
import com.arg.ccra3.online.service.TrnJsonService;
import com.arg.ccra3.online.service.ViewApiUserService;
import com.arg.ccra3.online.util.AuthenUtil;
 
@RestController
public class AbstractCtrl {
    protected AbstractCtrl(){}
    public static TrnJsonService trnJson = new TrnJsonService();
    public static MessageErrorService messageErrorService = new MessageErrorService();
    public static ViewApiUserService userService = new ViewApiUserService();
    public static CtrlKeyService ctrlKey = new CtrlKeyService();
    
    protected static AuthenUtil authenUtil = new AuthenUtil(trnJson, messageErrorService, userService, ctrlKey);
}
