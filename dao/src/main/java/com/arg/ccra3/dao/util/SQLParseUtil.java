
package com.arg.ccra3.dao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLParseUtil {
    private SQLParseUtil() {throw new AssertionError("Suppress default constructor for noninstantiability");}
    public static String getSQLString(String str){
        Pattern p;
        Matcher m;
        // replace Single Quote
        p = Pattern.compile("\'");
        m = p.matcher(str);
        str = m.replaceAll("\'\'");
        //replace [ with [[]
        p = Pattern.compile("\\[");
        m = p.matcher(str);
        str = m.replaceAll("[[]");
        // replace Under Scroll _
        p = Pattern.compile("_");
        m = p.matcher(str);
        str = m.replaceAll("[_]");
        //replace % with [%]
        p = Pattern.compile("%");
        m = p.matcher(str);
        str = m.replaceAll("[%]");

        return str;
    }
}
