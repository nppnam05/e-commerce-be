package com.e_commerce.e_commerce_api.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CookieUtils {

    /**
     * Thêm cookie với đầy đủ bảo mật: HttpOnly, SameSite=Strict.
     * Secure được truyền từ ngoài vào (profile-based: false=dev, true=prod).
     */
    public static void addCookie(HttpServletResponse response, String name, String value,
            int maxAgeSeconds, boolean secure) {
        String cookie = buildCookieHeader(name, value, maxAgeSeconds, secure);
        response.addHeader("Set-Cookie", cookie);
    }

    /**
     * Xoá cookie bằng cách set MaxAge=0.
     */
    public static void deleteCookie(HttpServletResponse response, String name, boolean secure) {
        String cookie = buildCookieHeader(name, "", 0, secure);
        response.addHeader("Set-Cookie", cookie);
    }

    private static String buildCookieHeader(String name, String value, int maxAgeSeconds, boolean secure) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(value);
        sb.append("; Max-Age=").append(maxAgeSeconds);
        sb.append("; Path=/");
        sb.append("; HttpOnly");
        sb.append("; SameSite=Strict");
        if (secure) {
            sb.append("; Secure");
        }
        return sb.toString();
    }
}