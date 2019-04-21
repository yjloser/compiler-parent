package com.semitransfer.compiler.core.config.internal.api;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * <p>
 *封装request、response
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class BaseRquestAndResponse {

    /**
     * 获取request请求
     *
     * @return 返回HttpServletRequest对象
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }


    /**
     * 获取response请求
     *
     * @return 返回HttpServletResponse对象
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取session请求
     *
     * @return 返回HttpSession对象
     * @author Mr.Yang
     * @date 2018/12/2
     */
    public static HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }

}
