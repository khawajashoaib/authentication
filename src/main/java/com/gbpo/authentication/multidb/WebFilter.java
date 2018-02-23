package com.gbpo.authentication.multidb;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by Asad on 2/16/2018.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(WebFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("Initiating WebFilter >> ");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String companyId = ((HttpServletRequest)request).getHeader("companyId");
        if(companyId == null) {
            chain.doFilter(request, response);
            return;
        }
        // Set in the Thread Context of the Request:
        ThreadLocalStorage.setCompanyId(Long.parseLong(companyId));
        //doFilter
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.debug("Destroying WebFilter >> ");
    }
}