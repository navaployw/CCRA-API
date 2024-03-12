
package com.arg.ccra3.dao.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class ReportResourceJson {
    private ReportResourceJson(){throw new AssertionError("no");}
    private static final String BUNDLE_EN = "com.arg.ccra3.dao.inquiry.app-resources";
    private static final String BUNDLE_LO = "com.arg.ccra3.dao.inquiry.app-resources_zh";
    
    private static final ResourceBundle RESOURCE_BUNDLE_EN =
        ResourceBundle.getBundle(BUNDLE_EN);
    private static final ResourceBundle RESOURCE_BUNDLE_LO =
        ResourceBundle.getBundle(BUNDLE_LO);


    public static String getMessage(Locale locale, String key)
    {
        ResourceBundle bundle = Locale.ENGLISH.equals(locale) ? RESOURCE_BUNDLE_EN : RESOURCE_BUNDLE_LO;
        if(key == null)throw new IllegalArgumentException("key is null");
        return bundle.getString(key);
    }
}
