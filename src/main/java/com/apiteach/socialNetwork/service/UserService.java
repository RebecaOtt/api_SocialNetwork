package com.apiteach.socialNetwork.service;

import com.apiteach.socialNetwork.domain.Role;
import com.apiteach.socialNetwork.dto.req.LoginReqDTO;
import com.apiteach.socialNetwork.dto.req.UserPatchDTOReq;
import com.apiteach.socialNetwork.dto.req.UserReqDTO;
import com.apiteach.socialNetwork.dto.res.LoginResDTO;
import com.apiteach.socialNetwork.dto.res.UserResDTO;
import com.apiteach.socialNetwork.exception.InvalidParamException;
import com.apiteach.socialNetwork.exception.ResourceAlreadyExistsException;
import com.apiteach.socialNetwork.exception.ResourceNotFoundException;
import com.apiteach.socialNetwork.exception.UnauthorizedAccessException;
import com.apiteach.socialNetwork.model.User;
import com.apiteach.socialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserResDTO createUser(UserReqDTO dto) {
        validateLogin(dto.username(), dto.password());
        validateMailAndName(dto.mail(), dto.name());
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

        List<Role> roles = List.of(dto.role());
        user.setRoles(roles);

        String encondedPassword = passwordEncoder.encode(dto.password());
        user.setPassword(encondedPassword);

        User savedUser = userRepository.save(user);
        return UserResDTO.ModelToDTO(savedUser);
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
        return UserResDTO.ModelToDTO(updatedUser);
    }

    public void deleteUser(String username, String user) {
        if (!user.equals(username))
            throw new UnauthorizedAccessException("You do not have permission to delete this account");

        User userDeleted = findByIdEntity(username);

        userDeleted.setDeleted(true);
        userRepository.save(userDeleted);
    }

    public List<UserResDTO> findAllUsers() {
        List<User> list = userRepository.findAllByDeletedFalse();
        return list.stream().map(UserResDTO::ModelToDTO).toList();
    }

    public LoginResDTO login(LoginReqDTO dto) {
        validateLogin(dto.username(), dto.password());
        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedFalse(dto.username());

        if (optionalUser.isEmpty())
            throw new ResourceNotFoundException("User not found");

        User user = optionalUser.get();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        authenticationManager.authenticate(token);

        return tokenService.generateToken(user);
    }

    private void validateLogin(String username, String password) {
        if (username == null || username.isEmpty())
            throw  new InvalidParamException("Username cannot be null or empty");
        if (password == null || password.isEmpty())
            throw new InvalidParamException("Password cannot be null or empty");
    }

    private void validateMailAndName(String email, String name){
        if (email == null || email.isEmpty())
            throw new InvalidParamException("Email cannot be null or empty");
        if (name == null || name.isEmpty())
            throw  new InvalidParamException("Name cannot be null or empty");
        if (!email.contains("@"))
            throw new InvalidParamException("Invalid email: must contain @");
    }

    private User findByIdEntity(String username){
        return this.userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found or already deleted!!"));
    }
}
