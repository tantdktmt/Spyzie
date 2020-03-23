package com.tantd.spyzie.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by tantd on 8/22/2017.
 */
public class DateTimeUtils {

    public static int compareDate(Calendar calendar1, Calendar calendar2) {
        if (calendar1 == null || calendar2 == null) {
            return 0;
        }
        if (calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR)) {
            return calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
        } else if (calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH)) {
            return calendar1.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH);
        } else if (calendar1.get(Calendar.DAY_OF_MONTH) != calendar2.get(Calendar.DAY_OF_MONTH)) {
            return calendar1.get(Calendar.DAY_OF_MONTH) - calendar2.get(Calendar.DAY_OF_MONTH);
        } else {
            return 0;
        }
    }

    public static String getStandardTimeZoneOffSet(TimeZone tz) {
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        return String.format("%.1f", offsetInMillis / 3600000f);
    }
}
