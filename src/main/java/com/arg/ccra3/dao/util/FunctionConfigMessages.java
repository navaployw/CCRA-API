
package com.arg.ccra3.dao.util;
import com.arg.util.TextUtil;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FunctionConfigMessages {
    
    
    private static final String BUNDLE_NAME = "function-privilege";
    
    private static final ResourceBundle BUNDLE;
    static
    {
        ResourceBundle rb = null;
        try
        {
            
            rb = ResourceBundle.getBundle(BUNDLE_NAME);
        }
        catch (MissingResourceException e)
        {
            try
            {
                
                rb = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH);
            }
            catch (MissingResourceException e1)
            {
                String package_name;
                Package pkg = FunctionConfigMessages.class
                        .getPackage();
                if (null == pkg)
                {
                    int index;
                    package_name = FunctionConfigMessages.class.getName();
                    if (-1 == (index = package_name.lastIndexOf(".")))
                    {
                        throw e1;
                    }
                    package_name = package_name.substring(0, index);
                }
                else
                {
                    package_name = FunctionConfigMessages.class.getPackage()
                                                                   .getName();
                }
                String bundle_name = package_name + "." + BUNDLE_NAME;
                try
                {
                    
                    rb = ResourceBundle.getBundle(bundle_name);
                }
                catch (MissingResourceException x)
                {
                    
                    rb = ResourceBundle.getBundle(bundle_name, Locale.ENGLISH);
                }
            }
        }
        BUNDLE = rb;
    }
    
    
    private FunctionConfigMessages()
    {
    }
    
    
    public static String getString(final String key, final String[] vars)
    {
        try
        {
            return TextUtil.variableMessage(BUNDLE.getString(key), vars);
        }
        catch (MissingResourceException e)
        {
            return "**" + key + "**";
        }
    }
    
    public static String getString(final String key, final String vars)
    {
        return getString(key, new String[] { vars });
    }
    
    public static String getString(final String key)
    {
        try
        {
            return BUNDLE.getString(key);
        }
        catch (MissingResourceException e)
        {
            return "**" + key + "**";
        }
    }
}
