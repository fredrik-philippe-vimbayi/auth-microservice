package com.example.authservice.entity;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Email is a required field")
    @Email
    private String email;
    @NotBlank(message = "Password is a required field")
    @Size(min = 4, message = "Password must be minimum 4 characters long")
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User addRole(Role role) {
        this.roles.add(role);
        role.addUser(this);
        return this;
    }

    public User removeRole(Role role) {
        this.roles.remove(role);
        role.removeUser(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
