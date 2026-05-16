package com.apiteach.socialNetwork.service;

import com.apiteach.socialNetwork.dto.req.PostPatchDTOReq;
import com.apiteach.socialNetwork.dto.req.PostReqDTO;
import com.apiteach.socialNetwork.dto.res.PostResDTO;
import com.apiteach.socialNetwork.exception.InvalidParamException;
import com.apiteach.socialNetwork.exception.ResourceNotFoundException;
import com.apiteach.socialNetwork.exception.UnauthorizedAccessException;
import com.apiteach.socialNetwork.model.Post;
import com.apiteach.socialNetwork.model.User;
import com.apiteach.socialNetwork.repository.PostRepository;
import com.apiteach.socialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public PostResDTO createPost(PostReqDTO dto, String username) {
        User user = this.findByUsernameEntity(username);

        if (dto.title() == null || dto.title().isEmpty()) {
            throw new InvalidParamException("Title field cannot be null or empty");
        }
        if (dto.privy() == null) {
            throw new InvalidParamException("Privy field cannot be null");
        }

        Post post = dto.dtoToModel();
        post.setUser(user);

        Post savedPost = this.postRepository.save(post);
        return PostResDTO.ModelToDTO(savedPost);
    }

    public PostResDTO updatePost(Long id, PostReqDTO postReqDTO, String username1) {
        Post post = findByIdEntity(id);
        this.validateUser(post, username1);

        if (postReqDTO.privy() != null)
            post.setPrivy(postReqDTO.privy());
        if (postReqDTO.videoLink() != null)
            post.setVideoLink(postReqDTO.videoLink());
        if (postReqDTO.photoLink() != null)
            post.setPhotoLink(postReqDTO.photoLink());
        if (postReqDTO.description() != null)
            post.setDescription(postReqDTO.description());
        if (postReqDTO.title() != null)
            post.setTitle(postReqDTO.title());

        Post updatePost = this.postRepository.save(post);
        return PostResDTO.ModelToDTO(updatePost);
    }

    public List<PostResDTO> findAllPosts(String username) {
        List<Post> posts = this.postRepository.findByUserUsernameAndDeletedFalse(username);

        return posts.stream().map(PostResDTO::ModelToDTO).toList();
    }

    public PostResDTO updatePrivyPost(Long id, PostPatchDTOReq postPatchDTOReq, String username) {
        Post post = findByIdEntity(id);
        this.validateUser(post, username);

        if (postPatchDTOReq.privy() != null)
            post.setPrivy(postPatchDTOReq.privy());

        Post updatePost = this.postRepository.save(post);
        return PostResDTO.ModelToDTO(updatePost);
    }

    public void deletePost(Long id, String username) {
        Post post = findByIdEntity(id);
        this.validateUser(post, username);
        post.setDeleted(true);
        this.postRepository.save(post);
    }

    private void validateUser(Post post, String username1) {
        if (!post.getUser().getUsername().equals(username1)){
            throw new UnauthorizedAccessException("You don't have permission to access this task.");
        }
    }

    private User findByUsernameEntity(String username){
        return this.userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(()->
                new ResourceNotFoundException("User not found or already deleted!!"));
    }

    private Post findByIdEntity(Long id){
        return this.postRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with the provided ID"));
    }
}
