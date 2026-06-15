package com.e_commerce.e_commerce_api.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtils {
    public static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}
