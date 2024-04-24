package com.demo.technicaltestbackend.controllers;

import com.demo.technicaltestbackend.dtos.UserDto;
import com.demo.technicaltestbackend.responses.BasicResponse;
import com.demo.technicaltestbackend.responses.TokenResponse;
import com.demo.technicaltestbackend.services.AuthenticatedUserService;
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
import org.springframework.web.server.ServerErrorException;

@RestController
@RequestMapping("/api")
@Slf4j
public class AuthController {

    private final UserService userService;

    private final AuthenticatedUserService authUserService;

    private final JwtHelper jwtHelper;

    public AuthController(UserService userService, AuthenticatedUserService authUserService, JwtHelper jwtHelper) {
        this.userService = userService;
        this.authUserService = authUserService;
        this.jwtHelper = jwtHelper;
    }

    // Register
    @PostMapping("/register")
    public ResponseEntity<BasicResponse> register(@RequestBody UserDto userDto) throws BadRequestException {
        log.info("Register");
        UserDto user = userService.createUser(userDto);
        user.setPassword(null);

        return ResponseEntity.ok(BasicResponse
                .builder()
                .data(user)
                .message("User successfully created!").build());

    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserDto userDto, HttpServletResponse response) {
        UserDto loggedInUser = userService.login(userDto.getUsername(), userDto.getPassword());

        String accessToken = jwtHelper.generateAccessToken(loggedInUser.getId(), loggedInUser.getUsername());
        String refreshToken = jwtHelper.generateRefreshToken(loggedInUser.getId(), loggedInUser.getUsername());

        try {
            accessToken = AESHelper.encrypt(accessToken);
            refreshToken = AESHelper.encrypt(accessToken);

            log.info("Refresh token: " + refreshToken.length());
            authUserService.authenticate(loggedInUser, refreshToken);

            log.info("Authentication successful");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new ServerErrorException("Something went wrong!", e);
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
