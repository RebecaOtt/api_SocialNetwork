package com.apiteach.socialNetwork.dto.res;

import com.apiteach.socialNetwork.model.User;

import java.time.LocalDate;

public record UserResDTO(String username, String name, String mail, LocalDate createdAt) {
    public static UserResDTO ModelToDTO(User user){
        return new UserResDTO(user.getUsername(), user.getName(), user.getMail(), user.getCreatedAt());
    }
}
