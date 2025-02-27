package com.bank.msauthentication.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1.0/auth/register", "/v1.0/auth/login").permitAll()  // Rutas públicas
                        .anyRequest().authenticated() // Todas las demás requieren autenticación
                )
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable());
        http.cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()));
        return http.build();
    }

    /*
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())  // Deshabilita CSRF para permitir solicitudes sin estado
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/v1.0/auth/register", "/v1.0/auth/login").permitAll() // Permite acceso sin token
                        .anyExchange().authenticated() // Requiere autenticación para cualquier otro endpoint
                )
                .build();
    }
*/
}
