
package com.arg.ccra3.dao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLParseUtil {
    private SQLParseUtil() {throw new AssertionError("Suppress default constructor for noninstantiability");}
    public static String getSQLString(String str){
        Pattern p;
        Matcher m;
        p = Pattern.compile("\'");
        m = p.matcher(str);
        str = m.replaceAll("\'\'");
        p = Pattern.compile("\\[");
        m = p.matcher(str);
        str = m.replaceAll("[[]");
        p = Pattern.compile("_");
        m = p.matcher(str);
        str = m.replaceAll("[_]");
        p = Pattern.compile("%");
        m = p.matcher(str);
        str = m.replaceAll("[%]");

        return str;
    }
}
