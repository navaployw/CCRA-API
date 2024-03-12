package com.arg.ccra3.dao.util;

import com.arg.util.TextUtil;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ReportResource {
    private ReportResource(){
    }

    private static final String BUNDLE_NAME =
        "com.arg.ccra3.dao.inquiry.report-statement";

    private static final ResourceBundle RESOURCE_BUNDLE =
        ResourceBundle.getBundle(BUNDLE_NAME);


    public static String getString(String key){
        try
        {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }


    public static String getString(final String key, final String[] vars){
        try
        {
            return TextUtil.variableMessage(RESOURCE_BUNDLE.getString(key), vars);
        }
        catch (MissingResourceException e)
        {
            return "**" + key + "**";
        }
    }

    public static String getString(final String key, final String var1)
    {
        return getString(key, new String[] { var1 });
    }
}
