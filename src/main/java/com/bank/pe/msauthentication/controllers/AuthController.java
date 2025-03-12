package com.bank.pe.msauthentication.controllers;

import com.bank.pe.msauthentication.entity.UserEntity;
import com.bank.pe.msauthentication.model.AuthRequest;
import com.bank.pe.msauthentication.model.AuthResponse;
import com.bank.pe.msauthentication.service.UserService;
import com.bank.pe.msauthentication.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        return userService.login(authRequest)
                .map(userDetails -> {
                    String token = jwtUtil.generateToken(userDetails);
                    return ResponseEntity.ok(new AuthResponse(token));
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()); // Retorna 401 si el usuario no existe
    }


    @PostMapping("/register")
    public Mono<ResponseEntity<UserEntity>> register(@RequestBody AuthRequest authRequest) {
        return userService.createUser(authRequest.getUsername(), authRequest.getPassword(), authRequest.getRole())
                .map(ResponseEntity::ok);
    }

}
