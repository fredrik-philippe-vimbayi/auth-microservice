package com.example.authservice.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private Role role;

    private User user;

    private Validator validator;

    @BeforeEach
    public void init() {
        role = new Role("user");
        user = new User("angela@mymail.com", "angels@25");

        user.addRole(role);

        validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    @Test
    void whenAddRoleShouldReturnAddedRole() {
        var roles = user.getRoles();
        var users = role.getUsers();

        assertThat(roles.size()).isEqualTo(1);
        assertThat(roles).contains(new Role("user"));
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void whenRemoveRoleThenRoleShouldBeRemoved() {
        user.removeRole(role);

        var roles = user.getRoles();

        assertThat(roles).isEmpty();
    }

    @Test
    void whenNotEmptyPasswordThenNoConstraintViolations() {
        user.setPassword("password");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenEmptyPasswordThenTwoConstraintViolations() {
        user.setPassword("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(2);
    }

    @Test
    void whenNullPasswordThenOneConstraintViolations() {
        user.setPassword(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
    }
}