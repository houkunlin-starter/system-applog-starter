package com.houkunlin.system.applog.starter;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具类（获取当前请求的IP地址）
 *
 * @author HouKunLin
 * @date 2020/8/11 0011 15:06
 */
public class RequestUtil {
    private static final String[] IP_KEYS = new String[]{"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
    private static final String[] DEFAULT_LOCAL_IP6 = new String[]{"0:0:0:0:0:0:0:1", "::1"};

    private RequestUtil() {
    }

    /**
     * 获得当前请求对象
     *
     * @return 请求对象
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    /**
     * 获取当前请求的IP
     *
     * @return IP
     */
    public static String getRequestIp() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return getRequestIp(request);
        }
        return null;
    }

    /**
     * 获取请求的IP
     *
     * @param request 请求对象
     * @return IP
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = null;
        boolean hasIp = false;
        for (final String ipKey : IP_KEYS) {
            ip = request.getHeader(ipKey);
            hasIp = hasIp(ip);
            if (hasIp) {
                break;
            }
        }
        if (!hasIp) {
            ip = request.getRemoteAddr();
        }
        return obtainIp(ip);
    }

    /**
     * 判断IP是否为空。是否拥有IP
     *
     * @param ip IP内容
     * @return 结果
     */
    private static boolean hasIp(String ip) {
        return StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * 获取IP信息（去除本机的IPv6地址）
     *
     * @param ip IP内容
     * @return IP信息
     */
    private static String obtainIp(String ip) {
        for (final String defaultLocalIp : DEFAULT_LOCAL_IP6) {
            if (defaultLocalIp.equals(ip)) {
                return "127.0.0.1";
            }
        }
        return ip;
    }
}
