package com.apiteach.socialNetwork.dto.req;

import com.apiteach.socialNetwork.model.Post;

public record PostReqDTO(
        String title,
        String description,
        String photoLink,
        String videoLink,
        Boolean privy
) {
    public Post dtoToModel() {
        return new Post(title, description, photoLink, videoLink, privy);
    }
}