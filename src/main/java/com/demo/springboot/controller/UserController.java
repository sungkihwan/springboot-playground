package com.demo.springboot.controller;

import com.demo.springboot.dto.response.TokenRefresh;
import com.demo.springboot.dto.response.TokenResponse;
import com.demo.springboot.service.UserService;
import com.demo.springboot.dto.request.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(userService.login(userDto));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefresh tokenRefresh) {
        return ResponseEntity.ok().body(userService.refreshToken(tokenRefresh));
    }
}
