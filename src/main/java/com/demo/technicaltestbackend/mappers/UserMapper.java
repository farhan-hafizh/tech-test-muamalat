package com.demo.technicaltestbackend.mappers;

import com.demo.technicaltestbackend.dtos.UserDto;
import com.demo.technicaltestbackend.entities.User;

public class UserMapper {
    public static User mapToUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getPassword(),
        );
    }

    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername()
                user.getPassword()
        );
    }
}
