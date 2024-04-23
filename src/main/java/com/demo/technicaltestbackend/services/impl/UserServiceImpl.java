package com.demo.technicaltestbackend.services.impl;

import com.demo.technicaltestbackend.dtos.UserDto;
import com.demo.technicaltestbackend.entities.User;
import com.demo.technicaltestbackend.mappers.UserMapper;
import com.demo.technicaltestbackend.repositories.UserRepository;
import com.demo.technicaltestbackend.services.UserService;
import com.demo.technicaltestbackend.utils.PasswordHelper;
import lombok.extern.slf4j.Slf4j;

import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordHelper passwordHelper;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordHelper = new PasswordHelper();
    }

    @Override
    public UserDto createUser(UserDto userDto) throws BadRequestException {
        User existingUser = userRepository.findByUsername(userDto.getUsername());

        if (existingUser != null) {
            throw new BadRequestException("User with username already exists");
        }

        userDto.setPassword(passwordHelper.encode(userDto.getPassword()));

        User newUser = UserMapper.mapToUser(userDto);
        newUser = userRepository.save(newUser);
        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        return null;
    }

    @Override
    public UserDto login(String username, String password) {
        log.info("Login requested: {}", username);
        User foundUser = userRepository.findByUsername(username);

        if (!passwordHelper.matches(password, foundUser.getPassword())) {
            throw new BadCredentialsException("Username or password is wrong!");
        }

        return UserMapper.mapToUserDto(foundUser);
    }
}
