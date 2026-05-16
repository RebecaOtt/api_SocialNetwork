package com.apiteach.socialNetwork.controller;

import com.apiteach.socialNetwork.dto.req.PostPatchDTOReq;
import com.apiteach.socialNetwork.dto.req.PostReqDTO;
import com.apiteach.socialNetwork.dto.res.PostResDTO;
import com.apiteach.socialNetwork.model.User;
import com.apiteach.socialNetwork.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<PostResDTO> createPost(@RequestBody PostReqDTO dto, @AuthenticationPrincipal User user, UriComponentsBuilder uriComponentsBuilder) {
        PostResDTO newPost = this.postService.createPost(dto, user.getUsername());
        URI uri = uriComponentsBuilder.path("/posts/{id}").buildAndExpand(newPost.id()).toUri();

        return ResponseEntity.created(uri).body(newPost);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResDTO> updatePost (@PathVariable("id") Long id, @RequestBody PostReqDTO postReqDTO, @AuthenticationPrincipal User user){
        PostResDTO postModel = this.postService.updatePost(id, postReqDTO, user.getUsername());
        return ResponseEntity.ok(postModel);
    }

    @GetMapping
    public ResponseEntity<List<PostResDTO>> findAllPosts(@AuthenticationPrincipal User user) {
        List<PostResDTO> posts = this.postService.findAllPosts(user.getUsername());
        return ResponseEntity.ok(posts);
    }

    @PatchMapping("/{id}/privy")
    public ResponseEntity<PostResDTO> updatePrivyPost (@PathVariable("id") Long id, @RequestBody PostPatchDTOReq postPatchDTOReq, @AuthenticationPrincipal User user){
        PostResDTO postModel = this.postService.updatePrivyPost(id, postPatchDTOReq, user.getUsername());
        return ResponseEntity.ok(postModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal User user){
        this.postService.deletePost(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
