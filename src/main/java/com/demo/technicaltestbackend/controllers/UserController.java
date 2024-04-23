package com.demo.technicaltestbackend.controllers;

import com.demo.technicaltestbackend.dtos.UserDto;
import com.demo.technicaltestbackend.responses.BaseResponse;
import com.demo.technicaltestbackend.responses.BasicResponse;
import com.demo.technicaltestbackend.responses.TokenResponse;
import com.demo.technicaltestbackend.services.UserService;
import com.demo.technicaltestbackend.utils.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final JwtHelper jwtHelper;

    public UserController(UserService userService, JwtHelper jwtHelper) {
        this.userService = userService;
        this.jwtHelper = jwtHelper;
    }

    // Register
    @PostMapping("/register")
    public ResponseEntity<BasicResponse>register(@RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto);

        return ResponseEntity.ok(BasicResponse
                .builder()
                .message("User successfully created!").build());

    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserDto userDto) {
        UserDto loggedInUser = userService.login(userDto.getUsername(), userDto.getPassword());

        String accessToken = jwtHelper.generateAccessToken(loggedInUser.getId());
        String refreshToken = jwtHelper.generateRefreshToken(loggedInUser.getId());

        return ResponseEntity.ok(
                TokenResponse
                        .builder()
                        .accessToken(accessToken)
                        .message("Successfully loggedIn!")
                        .build());
    }
}