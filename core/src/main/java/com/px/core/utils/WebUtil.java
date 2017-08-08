package com.px.core.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by veryyoung on 2015/12/9.
 */
public class WebUtil {


    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");

        if (ipUnknow(ip)) {
            if (ipUnknow(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ipUnknow(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipUnknow(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ipUnknow(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ipUnknow(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    private static boolean ipUnknow(String ip) {
        return StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
    }


    /**
     * 判断是否为Ajax请求
     *
     * @param request HttpServletRequest
     * @return 是true, 否false
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if (StringUtils.isNotEmpty(requestType) && requestType.equals("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}
