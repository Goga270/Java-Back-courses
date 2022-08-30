package com.example.experienceexchange.util.date;

import java.time.Instant;
import java.util.Date;

public class DateUtil {

    private final static String datePattern = "dd-MM-yyyy hh:mm:ss Z";

    public static Date dateTimeNow() {
        return Date.from(Instant.now());
    }
}
