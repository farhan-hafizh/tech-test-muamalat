package com.demo.technicaltestbackend.services.impl;

import org.springframework.stereotype.Service;

import com.demo.technicaltestbackend.dtos.UserDto;
import com.demo.technicaltestbackend.entities.AuthenticatedUser;
import com.demo.technicaltestbackend.repositories.AuthenticatedUserRepository;
import com.demo.technicaltestbackend.services.AuthenticatedUserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {

    AuthenticatedUserRepository authUserRepo;

    public AuthenticatedUserServiceImpl(AuthenticatedUserRepository authUserRepo) {
        this.authUserRepo = authUserRepo;
    }

    @Override
    public AuthenticatedUser authenticate(UserDto user, String refreshToken) {
        AuthenticatedUser newLoggedIn = new AuthenticatedUser();
        newLoggedIn.setUserId(user.getId());
        newLoggedIn.setUsername(user.getUsername());
        newLoggedIn.setRefreshToken(refreshToken);

        authUserRepo.save(newLoggedIn);

        return newLoggedIn;
    }

    @Override
    public AuthenticatedUser getByUsername(String username) {
        return authUserRepo.findByUsername(username);
    }

}
