package com.mmall.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebContentFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        //设置跨域请求
        HttpServletResponse response = (HttpServletResponse) res;
        //此处ip地址为需要访问服务器的ip及端口号
        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8080");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type,Token,Accept, Connection, User-Agent, Cookie");
        response.setHeader("Access-Control-Max-Age", "3628800");

        chain.doFilter(req, response);

    }


    @Override
    public void destroy() {

    }
}
