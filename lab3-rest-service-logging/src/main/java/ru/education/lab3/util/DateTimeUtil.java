package ru.education.lab3.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public final class DateTimeUtil {
    private DateTimeUtil() {
    }

    // A fresh formatter per request: SimpleDateFormat is not thread-safe.
    public static SimpleDateFormat getCustomFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }
}
