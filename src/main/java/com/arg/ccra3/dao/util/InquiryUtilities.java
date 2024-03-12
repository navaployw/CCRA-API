package com.arg.ccra3.dao.util;

import com.arg.ccra3.dao.security.util.ServerConfiguration;
import com.arg.util.TextUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class InquiryUtilities {

    private InquiryUtilities() {
    }
    
    
    public static String backToPeriod(String period, int year)
    {
        
        if ((null == period) || (period.length() < 6))
        {
            return getPeriod(0);
        }

        try
        {
            int pYear = Integer.parseInt(period.substring(0, 4));
            int pMonth = Integer.parseInt(period.substring(4, period.length()));

            return (pYear - year) + ""
            + TextUtil.leftPad(String.valueOf(pMonth), '0', 2);
        }
        catch (NumberFormatException ignore)
        {
            return getPeriod(0);
        }
    }

    
    public static Calendar getDateBackFromCurrent(int duration, int durationUnit)
    {
        Calendar now = Calendar.getInstance(Locale.ENGLISH);

        now.setTime(new Date());

        if (durationUnit == Calendar.MONTH)
        {
            now.add(Calendar.MONTH, -1 * duration);

            return now;
        }

        if ((durationUnit == Calendar.DAY_OF_YEAR)
                || (durationUnit == Calendar.DAY_OF_MONTH)
                || (durationUnit == Calendar.DAY_OF_WEEK)
                || (durationUnit == Calendar.DAY_OF_WEEK_IN_MONTH))
        {
            now.add(Calendar.DAY_OF_YEAR, -1 * duration);

            return now;
        }

        return null;
    }

    
    public static String getPeriod(int year)
    {
        
        Calendar cal =
            Calendar.getInstance(new Locale(ServerConfiguration.getString(
                        "locale.language"),
                    ServerConfiguration.getString("locale.country")));
        cal.add(Calendar.YEAR, -year);

        return cal.get(Calendar.YEAR)
        + TextUtil.leftPad(String.valueOf(cal.get(Calendar.MONTH) + 1), '0', 2);
    }

    
    public static String getPeriod(int year, int month)
    {
        
        Calendar cal =
            Calendar.getInstance(new Locale(ServerConfiguration.getString(
                        "locale.language"),
                    ServerConfiguration.getString("locale.country")));
        cal.add(Calendar.YEAR, -year);

        return cal.get(Calendar.YEAR)
        + TextUtil.leftPad(String.valueOf(cal.get(Calendar.MONTH) + month),
            '0', 2);
    }

    
    public static String getPeriod(Date date)
    {
        Calendar cal =
            Calendar.getInstance(new Locale(ServerConfiguration.getString(
                        "locale.language"),
                    ServerConfiguration.getString("locale.country")));
        cal.setTime(date);

        return cal.get(Calendar.YEAR)
        + TextUtil.leftPad(String.valueOf(cal.get(Calendar.MONTH)), '0', 2);
    }
}
