package com.ruoyi.common.i18n;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class i18Filter implements Filter {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String language = httpRequest.getHeader("Language");
        if (language != null && !language.isEmpty()) {
            LanguageContextHolder.setUserLanguage(language);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
