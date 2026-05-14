package com.apiteach.socialNetwork.config.security;

import com.apiteach.socialNetwork.model.User;
import com.apiteach.socialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedFalse(username);

        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException("User not found!");

        return optionalUser.get();

    }
}
