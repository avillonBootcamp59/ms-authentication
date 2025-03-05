package com.bank.msauthentication.service;


import com.bank.msauthentication.util.JwtUtil;
import com.bank.msauthentication.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

  /*  public Single<String> registerUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return Single.fromPublisher(
                        userRepository.save(user)
                ).subscribeOn(Schedulers.io()) // Ejecuta en otro hilo
                .map(UserEntity::getId);
    }

    public Single<String> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .subscribeOn(Schedulers.io())
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> jwtUtil.generateToken(username))
                .switchIfEmpty(Single.error(new RuntimeException("Credenciales inválidas")));
    }
*/

}

