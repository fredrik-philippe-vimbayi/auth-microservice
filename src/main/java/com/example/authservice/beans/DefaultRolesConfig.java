package com.example.authservice.beans;

import com.example.authservice.entity.Role;
import com.example.authservice.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultRolesConfig {

    @Bean
    public CommandLineRunner createDefaultRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ROLE_ADMIN") == null)
                roleRepository.save(new Role("ROLE_ADMIN"));
            if (roleRepository.findByName("ROLE_USER") == null)
                roleRepository.save(new Role("ROLE_USER"));
        };
    }

}
