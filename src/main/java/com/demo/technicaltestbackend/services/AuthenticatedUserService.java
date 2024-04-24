package com.demo.technicaltestbackend.services;

import com.demo.technicaltestbackend.dtos.UserDto;
import com.demo.technicaltestbackend.entities.AuthenticatedUser;

public interface AuthenticatedUserService {
    AuthenticatedUser authenticate(UserDto user, String refreshToken);

    AuthenticatedUser getByUsername(String username);
}
