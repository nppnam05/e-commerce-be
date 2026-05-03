package com.e_commerce.e_commerce_api.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateTimeUtils {

    public static LocalDateTime toLocalDateTime(long milliseconds) {
        ZoneId zoneVietNam = ZoneId.of("Asia/Ho_Chi_Minh");
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), zoneVietNam);
    }
}