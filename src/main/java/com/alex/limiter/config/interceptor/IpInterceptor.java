package com.alex.limiter.config.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class IpInterceptor extends HandlerInterceptorAdapter {

    private final SessionData requestSessionData;

    public IpInterceptor(SessionData requestSessionData) {
        this.requestSessionData = requestSessionData;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var ip = getRemoteAddr(request);
        var date = new Date();
        requestSessionData.setIp(ip);
        requestSessionData.setTime(date.getTime());
        log.info("IP: " + ip + ", time: " + date);
        return true;
    }

    private String getRemoteAddr(HttpServletRequest request) {
        var ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }

}
