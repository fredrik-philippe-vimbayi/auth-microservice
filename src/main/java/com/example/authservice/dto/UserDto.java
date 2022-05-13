package com.example.authservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public final class UserDto {
    @NotBlank(message = "is a required field")
    @Email
    private final String username;
    @NotBlank(message = "is a required field")
    @Min(value = 4, message = "must be minimum of 4 characters long")
    private final String password;

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserDto) obj;
        return Objects.equals(this.username, that.username) &&
                Objects.equals(this.password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "UserDto[" +
                "username=" + username + ", " +
                "password=" + password + ']';
    }

}
