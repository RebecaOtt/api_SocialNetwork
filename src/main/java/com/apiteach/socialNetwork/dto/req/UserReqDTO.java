package com.apiteach.socialNetwork.dto.req;

import com.apiteach.socialNetwork.domain.Role;

public record UserReqDTO (String username, String password, String name, String phone, String mail, String profileLink, Role role){
}
