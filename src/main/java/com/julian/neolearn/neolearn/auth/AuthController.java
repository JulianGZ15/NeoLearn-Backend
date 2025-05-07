package com.julian.neolearn.neolearn.auth;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:4200")
public class AuthController {

    private final AuthService authservice;

    @PostMapping(value = "login")
        public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
          return ResponseEntity.ok(authservice.login(request));
        }

        @PostMapping(value = "register")
        public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
            return ResponseEntity.ok(authservice.register(request));
        }
}

