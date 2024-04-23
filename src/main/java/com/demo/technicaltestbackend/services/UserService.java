package com.demo.technicaltestbackend.services;

import com.demo.technicaltestbackend.dtos.UserDto;
import com.demo.technicaltestbackend.entities.User;

public interface UserService {

    UserDto createUser(UserDto user);
    UserDto getUserById(Long id);
    UserDto login(String username, String password);
}
