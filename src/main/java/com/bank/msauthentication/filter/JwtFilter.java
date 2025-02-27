package com.bank.msauthentication.filter;
import com.bank.msauthentication.util.JwtUtil;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements WebFilter {


    private final JwtUtil jwtUtil;


    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange);


        if (token == null || token.isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Token"));
        }


        return validateToken(token)
                .flatMap(isValid -> {
                    if (isValid) {
                        return chain.filter(exchange);
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token"));
                    }
                });
    }


    private Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> jwtUtil.validateToken(token));
    }


    private String extractToken(ServerWebExchange exchange) {
        return exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
    }

}
