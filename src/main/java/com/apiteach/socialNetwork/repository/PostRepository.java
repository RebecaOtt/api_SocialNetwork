package com.apiteach.socialNetwork.repository;

import com.apiteach.socialNetwork.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserUsernameAndDeletedFalse(String username);
}
