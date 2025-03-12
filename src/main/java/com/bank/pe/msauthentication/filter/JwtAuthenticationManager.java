package com.bank.pe.msauthentication.filter;
import com.bank.pe.msauthentication.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.ArrayList;

@Slf4j
@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = jwtUtil.extractUsername(authToken);

        if (username != null && jwtUtil.validateToken(authToken, new User(username, "", new ArrayList<>()))) {
            return Mono.just(new UsernamePasswordAuthenticationToken(username, authToken, new ArrayList<>()));
        } else {
            return Mono.empty();
        }
    }
}
