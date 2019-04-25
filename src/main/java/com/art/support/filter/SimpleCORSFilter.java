package com.art.support.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* author fangzhongming
* createDate 19/3/23
*/
@Component
@WebFilter(urlPatterns = {"/api/*"})
public class SimpleCORSFilter implements Filter {

    private Log log = LogFactory.getLog(getClass());

    public SimpleCORSFilter() {
        log.info("SimpleCORSFilter inited");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Accept, Origin, XRequestedWith, Content-Type, projectx-token");
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
