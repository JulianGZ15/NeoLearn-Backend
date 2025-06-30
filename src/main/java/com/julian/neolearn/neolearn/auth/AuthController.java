package com.julian.neolearn.neolearn.auth;

import org.springframework.web.bind.annotation.RestController;

import com.julian.neolearn.neolearn.dto.EmpresaDTO;
import com.julian.neolearn.neolearn.dto.RegisterRequestWrapper;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authservice;

    @PostMapping(value = "login")
        public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
          return ResponseEntity.ok(authservice.login(request));
        }

@PostMapping(value = "register")
public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequestWrapper requestWrapper){
    return ResponseEntity.ok(authservice.register(requestWrapper.getUser(), requestWrapper.getEmpresa()));
}

}

