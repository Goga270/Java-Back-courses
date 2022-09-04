package com.example.experienceexchange.util.date;

import java.time.Instant;
import java.util.Date;

public class DateUtil {

    private final static String datePattern = "dd-MM-yyyy hh:mm:ss Z";

    public static Date dateTimeNow() {
        return Date.from(Instant.now());
    }

    public static Boolean isDateBeforeNow(Date date) {
        return date.before(dateTimeNow());
    }

    public static Boolean isDateAfterNow(Date date) {
        return date.after(dateTimeNow());
    }
}
