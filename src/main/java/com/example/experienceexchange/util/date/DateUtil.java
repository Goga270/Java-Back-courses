package com.example.experienceexchange.util.date;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateUtil {

    public Date dateTimeNow() {
        return Date.from(Instant.now());
    }

    public Boolean isDateBeforeNow(Date date) {
        return date.before(dateTimeNow());
    }

    public Boolean isDateAfterNow(Date date) {
        return date.after(dateTimeNow());
    }

    public Date addDays(Date date, Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}
