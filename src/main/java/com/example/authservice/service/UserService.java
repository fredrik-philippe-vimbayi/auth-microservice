package com.example.authservice.service;

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
    private final String DEFAULT_ROLE = "ROLE_USER";

    public UserService(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(User user) {
        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE);
        user.addRole(defaultRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
