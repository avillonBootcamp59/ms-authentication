package com.bank.pe.msauthentication.model;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String role;
}
