package com.demo.technicaltestbackend.services;

import org.apache.coyote.BadRequestException;

import com.demo.technicaltestbackend.dtos.UserDto;

public interface UserService {

    UserDto createUser(UserDto user) throws BadRequestException;

    UserDto getUserById(Long id);

    UserDto login(String username, String password);
}
