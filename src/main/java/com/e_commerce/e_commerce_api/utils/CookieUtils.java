package com.e_commerce.e_commerce_api.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CookieUtils {

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null)
            return null;
        return Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(name))
                .map(Cookie::getValue).findFirst().orElse(null);
    }

    // Secure được truyền từ ngoài vào (profile-based: false=dev, true=prod).
    public static void addCookie(HttpServletResponse response, String name, String value,
            int maxAgeSeconds, boolean secure) {
        String cookie = buildCookieHeader(name, value, maxAgeSeconds, secure);
        response.addHeader("Set-Cookie", cookie);
    }

    public static void deleteCookie(HttpServletResponse response, String name, boolean secure) {
        String cookie = buildCookieHeader(name, "", 0, secure);
        response.addHeader("Set-Cookie", cookie);
    }

    // cookie với đầy đủ bảo mật: HttpOnly, SameSite=Strict.
    private static String buildCookieHeader(String name, String value, int maxAgeSeconds,
            boolean secure) {
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
