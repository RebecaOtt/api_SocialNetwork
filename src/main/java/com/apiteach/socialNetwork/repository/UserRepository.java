package com.apiteach.socialNetwork.repository;

import com.apiteach.socialNetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsernameAndDeletedFalse(String username);

    boolean existsByMailAndDeletedFalse(String mail);

    boolean existsByPhoneAndDeletedFalse(String phone);

    Optional<User> findByUsernameAndDeletedFalse(String username);

    List<User> findAllByDeletedFalse();
}
