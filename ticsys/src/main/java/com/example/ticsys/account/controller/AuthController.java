package com.example.ticsys.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticsys.account.dto.request.SignInRequest;
import com.example.ticsys.account.dto.response.SignInRepsonse;
import com.example.ticsys.account.service.AuthService;

@RestController
@RequestMapping("/api/account/auth")
public class AuthController {
    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }
    @PostMapping("/signin")
    public ResponseEntity<SignInRepsonse> signin(@RequestBody SignInRequest signInRequest)
    {
        var result = authService.signin(signInRequest);

        return ResponseEntity.ok(result);
    }
    
}
