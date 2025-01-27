
package com.arg.ccra3.online.rest;

import com.arg.ccra3.online.service.CdiTokenService;
import com.arg.ccra3.online.service.CtrlKeyService;
import com.arg.ccra3.online.service.MessageErrorService;
import com.arg.ccra3.online.service.TrnJsonService;
import com.arg.ccra3.online.service.ViewApiUserService;
import com.arg.ccra3.online.util.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AbstractCtrl {

    public static TrnJsonService trnJson = new TrnJsonService();
    public static CdiTokenService cdiTokenService = new CdiTokenService();
    public static MessageErrorService messageErrorService = new MessageErrorService();
    public static ViewApiUserService userService = new ViewApiUserService();
    public static CtrlKeyService ctrlKey = new CtrlKeyService();
    
    protected static AuthenUtil authenUtil = new AuthenUtil(trnJson, cdiTokenService, messageErrorService, userService, ctrlKey);
}
