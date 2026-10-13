package ru.education.lab4.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public final class DateTimeUtil {
    private DateTimeUtil() { }

    public static SimpleDateFormat getCustomFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter;
    }
}
