package com.apiteach.socialNetwork.dto.res;

import com.apiteach.socialNetwork.model.Post;

import java.time.LocalDate;

public record PostResDTO(
        Long id,
        String title,
        String description,
        String photoLink,
        String videoLink,
        Boolean privy,
        LocalDate createdAt
) {
    public static PostResDTO ModelToDTO(Post post) {
        return new PostResDTO(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getPhotoLink(),
                post.getVideoLink(),
                post.getPrivy(),
                post.getCreatedAt()
        );
    }
}