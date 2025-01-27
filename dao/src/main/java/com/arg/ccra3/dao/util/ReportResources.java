package com.arg.ccra3.dao.util;

import com.arg.cb2.util.XMLUtil;
import com.arg.util.TextUtil;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class ReportResources {
    private static final String BUNDLE_NAME_EN = "report-resources";
    private static final String BUNDLE_NAME_ZH = "report-resources_zh";

    private static final ResourceBundle BUNDLE_EN;
    private static final ResourceBundle BUNDLE_ZH;

    private static StringBuffer sbEn = new StringBuffer();
    private static StringBuffer sbZh = new StringBuffer();

    static
    {
        ResourceBundle rb = null;

        try
        {

            rb = ResourceBundle.getBundle(BUNDLE_NAME_EN);
        }
        catch (MissingResourceException e)
        {
            try
            {

                rb = ResourceBundle.getBundle(BUNDLE_NAME_EN, Locale.ENGLISH);
            }
            catch (MissingResourceException e1)
            {
                String package_name;
                Package pkg = OIStatement.class.getPackage();

                if (null == pkg)
                {
                    int index;

                    if (-1 == (index = (package_name = OIStatement.class.getName())
                                .lastIndexOf(".")))
                    {
                        throw e1;
                    }

                    package_name = package_name.substring(0, index);
                }
                else
                {
                    package_name = OIStatement.class.getPackage().getName();
                }

                String bundle_name = package_name + "." + BUNDLE_NAME_EN;

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

        BUNDLE_EN = rb;

        sbEn.append("<resources>\n");

        String key = null;

        for (Enumeration enu = BUNDLE_EN.getKeys(); enu.hasMoreElements();)
        {
            key = (String) enu.nextElement();
            XMLUtil.getCData(BUNDLE_EN.getString(key), key, sbEn);
            sbEn.append("\n");
        }

        sbEn.append("</resources>");

        rb = null;

        try
        {

            rb = ResourceBundle.getBundle(BUNDLE_NAME_ZH);
        }
        catch (MissingResourceException e)
        {
            try
            {

                rb = ResourceBundle.getBundle(BUNDLE_NAME_ZH, Locale.ENGLISH);
            }
            catch (MissingResourceException e1)
            {
                String package_name;
                Package pkg = OIStatement.class.getPackage();

                if (null == pkg)
                {
                    int index;

                    if (-1 == (index = (package_name = OIStatement.class.getName())
                                .lastIndexOf(".")))
                    {
                        throw e1;
                    }

                    package_name = package_name.substring(0, index);
                }
                else
                {
                    package_name = OIStatement.class.getPackage().getName();
                }

                String bundle_name = package_name + "." + BUNDLE_NAME_ZH;

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

        BUNDLE_ZH = rb;

        sbZh.append("<resources>\n");

        key = null;

        for (Enumeration enu = BUNDLE_ZH.getKeys(); enu.hasMoreElements();)
        {
            key = (String) enu.nextElement();
            XMLUtil.getCData(BUNDLE_ZH.getString(key), key, sbZh);
            sbZh.append("\n");
        }

        sbZh.append("</resources>");
    }

    //~ Constructors -----------------------------------------------------------


    private ReportResources()
    {
    }

    //~ Methods ----------------------------------------------------------------


    public static String getString(final String key, final String[] vars)
    {
        return getString(key, vars, Locale.ENGLISH);
    }


    public static String getString(final String key, final String[] vars,
        Locale locale)
    {
        try
        {
            if (Locale.ENGLISH.equals(locale))
            {
                return TextUtil.variableMessage(BUNDLE_EN.getString(key), vars);
            }
            else
            {
                return TextUtil.variableMessage(BUNDLE_ZH.getString(key), vars);
            }
        }
        catch (MissingResourceException e)
        {
            return "**" + key + "**";
        }
    }


    public static String getString(final String key, final String var)
    {
        return getString(key, new String[] { var });
    }


    public static String getString(final String key, final String var,
        Locale locale)
    {
        return getString(key, new String[] { var }, locale);
    }


    public static String getString(final String key)
    {
        return getString(key, Locale.ENGLISH);
    }


    public static String getString(final String key, Locale locale)
    {
        try
        {
            if (Locale.ENGLISH.equals(locale))
            {
                return BUNDLE_EN.getString(key);
            }
            else
            {
                return BUNDLE_ZH.getString(key);
            }
        }
        catch (MissingResourceException e)
        {
            return "**" + key + "**";
        }
    }


    public static String toXML()
    {
        return toXML(Locale.ENGLISH);
    }


    public static String toXML(Locale locale)
    {
        if (Locale.ENGLISH.equals(locale))
        {
            return sbEn.toString();
        }
        else
        {
            return sbZh.toString();
        }
    }
}
