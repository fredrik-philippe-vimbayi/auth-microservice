package com.example.authservice.service;

import com.example.authservice.dto.NewUser;
import com.example.authservice.dto.UserDto;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String DEFAULT_ROLE = "ROLE_USER";

    public UserService(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public NewUser createUser(UserDto userDetails) {
        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE);
        User user = new User().setUsername(userDetails.username())
                .setPassword(passwordEncoder.encode(userDetails.password()))
                .addRole(defaultRole);
        User savedUser = userRepository.save(user);
        return new NewUser(savedUser.getUsername());
    }

}
