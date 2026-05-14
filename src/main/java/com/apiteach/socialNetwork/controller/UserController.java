package com.apiteach.socialNetwork.controller;

import com.apiteach.socialNetwork.dto.req.LoginReqDTO;
import com.apiteach.socialNetwork.dto.req.UserPatchDTOReq;
import com.apiteach.socialNetwork.dto.req.UserReqDTO;
import com.apiteach.socialNetwork.dto.res.LoginResDTO;
import com.apiteach.socialNetwork.dto.res.UserResDTO;
import com.apiteach.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public UserResDTO updateUser(@PathVariable String username, @RequestBody UserPatchDTOReq dto) {
        return userService.updateUser(username, dto);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
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
