package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = ".image.com";
    private final static String COOKIE_NAME = "mmall_login_token";


    //设置cookie
    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck = new Cookie(COOKIE_NAME,token);
        //其他域名下都可以访问cookie   http://lc-bsp.jszx.com:8080/webapp_b和http://localhost:8080/webapp_b
        //都可以访问到cookie
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");//代表设置在根目录 可在同一应用服务器内共享
        //时间.单位秒,如果不设置只写入内存只在当前页面有效 ,设置写入硬盘
        ck.setMaxAge(60*60*24);
        //通过JavaScript脚本将无法读取到Cookie信息，这样能有效的防止XSS攻击
        ck.setHttpOnly(true);
        log.info("cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);
    }
    //获取cookie
    public static String readLoginToken(HttpServletRequest request){
        Cookie [] cks = request.getCookies();
        if(cks!=null){
            for(Cookie ck : cks){
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }
    //删除cookie
    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie [] cks = request.getCookies();
        if(cks != null){
            for(Cookie ck : cks){
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setMaxAge(0);
                    ck.setPath("/");
                    log.info("del cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }
}
