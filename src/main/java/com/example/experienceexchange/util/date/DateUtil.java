package com.example.experienceexchange.util.date;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date dateTimeNow() {
        return Date.from(Instant.now());
    }

    public static Boolean isDateBeforeNow(Date date) {
        return date.before(dateTimeNow());
    }

    public static Boolean isDateAfterNow(Date date) {
        return date.after(dateTimeNow());
    }

    public static Date addDays(Date date, Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}
