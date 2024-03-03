package com.learnings.multiTenant.MultiTenantDemo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Order(1)
@Slf4j
class TenantFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {
        log.info("==============TenantFilter::start================");
        HttpServletRequest req = (HttpServletRequest) request;
        String tenantName = Optional.ofNullable(req.getHeader("orgId"))
                .filter(s -> s != null && !s.isEmpty())
                .orElse("default");
        log.info("tenantName: {}", tenantName);
        TenantContext.setCurrentTenant(tenantName);
        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.setCurrentTenant("");
        }
        log.info("==============TenantFilter::end================");
    }


    @Override
    public void destroy() {
    }
}
