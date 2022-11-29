package com.hospital.review.domain.dto;

import com.hospital.review.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;
    private String email;

    public User toEntity(){
        return User.builder()
                .userName(this.userName)
                .emailAddress(this.email)
                .build();
    }
}
