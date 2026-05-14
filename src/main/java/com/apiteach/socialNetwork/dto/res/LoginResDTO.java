package com.apiteach.socialNetwork.dto.res;

public record LoginResDTO(
        String type,
        String token,
        Long expiresAt
) {
}
