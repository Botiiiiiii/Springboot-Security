package com.hospital.review.service;

import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;

public class UserService {
    public UserDto join(UserJoinRequest request){
        return new UserDto("","","");
    }
}
