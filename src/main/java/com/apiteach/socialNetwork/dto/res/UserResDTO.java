package com.apiteach.socialNetwork.dto.res;

import java.time.LocalDate;

public record UserResDTO(String username, String name, String mail, LocalDate createdAt) {
}
