package com.bank.msauthentication.filter;
import com.bank.msauthentication.service.AuthService;
import com.bank.msauthentication.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.WebFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.ArrayList;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;


    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtUtil = jwtUtil;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;

            if (authHeader == null || authHeader.isEmpty()) {
                if (request.getRequestURI().contains( "/v1.0/auth/login") ||
                        request.getRequestURI().contains("/actuator/**")) {
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

            Claims claims = jwtUtil.decodeToken(jwt);

          /*  if (claims.get(ConstanteUtil.TOKEN_INF_TIPO, String.class).equals(ConstanteUtil.TOKEN_TIPO_REFRESHTOKEN) &&
                    !request.getRequestURI().contains(ConstanteUtil.PATH_SEGURIDAD_REFRESHTOKEN))
                throw new Exception401(ConstanteUtil.MENSAJE_ERROR_USO_INDEBIDO_REFRESHTOKEN);
*/
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
