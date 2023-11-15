package io.github.LuizMartendal.library.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private HandlerExceptionResolver resolver;

    private final JwtTokenProvider tokenProvider;

    public JwtTokenFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver, JwtTokenProvider jwtTokenProvider) {
        this.resolver = resolver;
        this.tokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenProvider.resolveToken(httpServletRequest);
            if (token != null) {
                Authentication authenticationToken = tokenProvider.getAuthentication(token);
                if (authenticationToken != null) {
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            resolver.resolveException(httpServletRequest, httpServletResponse, null, e);
        }
    }
}
