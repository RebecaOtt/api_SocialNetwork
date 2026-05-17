package com.apiteach.socialNetwork.controller;

import com.apiteach.socialNetwork.dto.req.LoginReqDTO;
import com.apiteach.socialNetwork.dto.req.UserPatchDTOReq;
import com.apiteach.socialNetwork.dto.req.UserReqDTO;
import com.apiteach.socialNetwork.dto.res.LoginResDTO;
import com.apiteach.socialNetwork.dto.res.UserResDTO;
import com.apiteach.socialNetwork.model.User;
import com.apiteach.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserResDTO createUser(@RequestBody UserReqDTO dto){
        UserResDTO response = userService.createUser(dto);
        return response;
    }

    @PatchMapping("/{username}")
    public UserResDTO updateUser(@PathVariable String username, @RequestBody UserPatchDTOReq dto, @AuthenticationPrincipal
    User user) {
        return userService.updateUser(username, dto, user.getUsername());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username, @AuthenticationPrincipal
                           User user) {
        this.userService.deleteUser(username, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResDTO>> findAllUsers() {
        List<UserResDTO> list = this.userService.findAllUsers();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/login")
    public LoginResDTO login(@RequestBody LoginReqDTO dto){
        LoginResDTO response = userService.login(dto);
        return response;
    }
}
