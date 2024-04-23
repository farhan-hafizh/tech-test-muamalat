package com.demo.technicaltestbackend.controllers;

import com.demo.technicaltestbackend.dtos.UserDto;
import com.demo.technicaltestbackend.responses.BasicResponse;
import com.demo.technicaltestbackend.responses.TokenResponse;
import com.demo.technicaltestbackend.services.UserService;
import com.demo.technicaltestbackend.utils.AESHelper;
import com.demo.technicaltestbackend.utils.JwtHelper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;

    private final JwtHelper jwtHelper;

    public UserController(UserService userService, JwtHelper jwtHelper) {
        this.userService = userService;
        this.jwtHelper = jwtHelper;
    }

    // Register
    @PostMapping("/register")
    public ResponseEntity<BasicResponse> register(@RequestBody UserDto userDto) throws BadRequestException {
        log.info("Register");
        UserDto user = userService.createUser(userDto);

        return ResponseEntity.ok(BasicResponse
                .builder()
                .data(user)
                .message("User successfully created!").build());

    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserDto userDto, HttpServletResponse response) {
        UserDto loggedInUser = userService.login(userDto.getUsername(), userDto.getPassword());

        String accessToken = jwtHelper.generateAccessToken(loggedInUser.getId());
        String refreshToken = jwtHelper.generateRefreshToken(loggedInUser.getId());

        try {
            accessToken = AESHelper.encrypt(accessToken,
                    "B6yGkT6V4mUa6w6R");
            refreshToken = AESHelper.encrypt(accessToken,
                    "B6yGkT6V4mUa6w6R");
            log.info(accessToken);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        response.addCookie(cookie);

        return ResponseEntity.ok(
                TokenResponse
                        .builder()
                        .accessToken(accessToken)
                        .message("Successfully loggedIn!")
                        .build());
    }
}
