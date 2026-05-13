package com.apiteach.socialNetwork.service;

import com.apiteach.socialNetwork.dto.req.UserPatchDTOReq;
import com.apiteach.socialNetwork.dto.req.UserReqDTO;
import com.apiteach.socialNetwork.dto.res.UserResDTO;
import com.apiteach.socialNetwork.exception.InvalidParamException;
import com.apiteach.socialNetwork.exception.ResourceAlreadyExistsException;
import com.apiteach.socialNetwork.exception.ResourceNotFoundException;
import com.apiteach.socialNetwork.model.User;
import com.apiteach.socialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResDTO createUser(UserReqDTO dto) {
        validate(dto.username(), dto.password(), dto.mail(), dto.name());
        if (userRepository.existsByUsernameAndDeletedFalse(dto.username()))
            throw new ResourceAlreadyExistsException("username already in use");
        if (userRepository.existsByMailAndDeletedFalse(dto.mail()))
            throw new ResourceAlreadyExistsException("email already in use");
        if (userRepository.existsByPhoneAndDeletedFalse(dto.phone()))
            throw new ResourceAlreadyExistsException("phone already in use");

        User user = new User();
        user.setUsername(dto.username());
        user.setMail(dto.mail());
        user.setName(dto.name());
        user.setPhone(dto.phone());
        user.setProfileLink(dto.profileLink());

        String encondedPassword = passwordEncoder.encode(dto.password());
        user.setPassword(encondedPassword);

        User savedUser = userRepository.save(user);
        return new UserResDTO(savedUser.getUsername(), savedUser.getName(), savedUser.getMail(), savedUser.getCreatedAt());
    }

    public void validate(String username, String password, String email, String name) {
        if (username == null || username.isEmpty())
            throw  new InvalidParamException("Username cannot be null or empty");
        if (password == null || password.isEmpty())
            throw new InvalidParamException("Password cannot be null or empty");
        if (email == null || email.isEmpty())
            throw new InvalidParamException("Email cannot be null or empty");
        if (name == null || name.isEmpty())
            throw  new InvalidParamException("Name cannot be null or empty");
        if (!email.contains("@"))
            throw new InvalidParamException("Invalid email: must contain @");
    }

    public UserResDTO updateUser(String username, UserPatchDTOReq dto) {
        User user = findByIdEntity(username);

        if (dto.mail() != null && !dto.mail().equals(user.getMail())) {
            if (userRepository.existsByMailAndDeletedFalse(dto.mail())) {
                throw new ResourceAlreadyExistsException("Email already in use by another user");
            }
            if (!dto.mail().contains("@")) {
                throw new InvalidParamException("Invalid email format");
            }
            user.setMail(dto.mail());
        }

        if (dto.name() != null) user.setName(dto.name());
        if (dto.phone() != null) user.setPhone(dto.phone());
        if (dto.profileLink() != null) user.setProfileLink(dto.profileLink());

        User updatedUser = userRepository.save(user);
        return new UserResDTO(updatedUser.getUsername(), updatedUser.getName(), updatedUser.getMail(), updatedUser.getCreatedAt());
    }

    public void deleteUser(String username) {
        User user = findByIdEntity(username);

        user.setDeleted(true);
        userRepository.save(user);
    }

    private User findByIdEntity(String username){
        return this.userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found or already deleted!!"));
    }

    public List<UserResDTO> findAllUsers() {
        List<User> users = userRepository.findAllByDeletedFalse();
        return users.stream()
                .map(user -> new UserResDTO(
                        user.getUsername(),
                        user.getName(),
                        user.getMail(),
                        user.getCreatedAt()))
                .toList();
    }
}
