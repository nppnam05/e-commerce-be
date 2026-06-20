package com.e_commerce.e_commerce_api.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateTimeUtils {

    private static final ZoneId ZONE_VIETNAM = ZoneId.of("Asia/Ho_Chi_Minh");

    public static LocalDateTime toLocalDateTime(long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZONE_VIETNAM);
    }

    public static LocalDateTime toDateTimeNow() {
        return LocalDateTime.now(ZONE_VIETNAM);
    }

    public static Date toDateNow() {
        ZonedDateTime vietnamTime = ZonedDateTime.now(ZONE_VIETNAM);
        return Date.from(vietnamTime.toInstant());
    }

    public static Date toDateExpired(long millisecondsToAdd) {
        ZonedDateTime vietnamTime =
                ZonedDateTime.now(ZONE_VIETNAM).plus(Duration.ofMillis(millisecondsToAdd));

        return Date.from(vietnamTime.toInstant());
    }
}
