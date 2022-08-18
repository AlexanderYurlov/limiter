package com.alex.limiter.utils;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {

    public static String getRemoteAddr(HttpServletRequest request) {
        var ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}
