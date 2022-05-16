package com.example.authservice.mapper;

import com.example.authservice.dto.NewUser;

public class UserMapper {

    public String userToJson(NewUser user) {
        return "{ \"username\" : \"" + user.username() + "\" }";
    }

}
