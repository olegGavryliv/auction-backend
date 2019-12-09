package com.havryliv.auction.converter;

import com.havryliv.auction.dto.UserDTO;
import com.havryliv.auction.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserConverter {

    public User fromDTOtoEntity(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .build();
    }

    public UserDTO fromEntityToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

    }
}
