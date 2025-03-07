package com.bank.pe.msauthentication.filter;
import com.bank.pe.msauthentication.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {


    private final JwtUtil jtUtil;


    private final HandlerExceptionResolver resolver;

    public JwtFilter(JwtUtil jtUtil,
                     @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jtUtil = jtUtil;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;

            if (authHeader == null || authHeader.isEmpty()) {
                if (request.getRequestURI().contains("/api/v1/auth/register") ||
                        request.getRequestURI().contains("/api/v1/auth/login") ||
                        request.getRequestURI().contains("/actuator")  ) {
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    resolver.resolveException(request, response, null, new BadCredentialsException(""));
                    return;
                }
            }

            if (!authHeader.startsWith("Bearer ")) {
                resolver.resolveException(request, response, null, new BadCredentialsException(""));
                return;
            }

            jwt = authHeader.substring(7);

            Claims claims = jtUtil.decodeToken(jwt);


            if (claims.getSubject() != null && !claims.getSubject().isEmpty() &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(claims.getSubject(), null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resolver.resolveException(request, response, null, new BadCredentialsException(""));
        }
    }
}
