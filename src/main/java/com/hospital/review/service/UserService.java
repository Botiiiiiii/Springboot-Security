package com.hospital.review.service;

import com.hospital.review.domain.User;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.domain.dto.UserLoginRequest;
import com.hospital.review.exception.ErrorCode;
import com.hospital.review.exception.AppException;
import com.hospital.review.repository.UserRepository;
import com.hospital.review.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String key;
    private long expiredTimeMs = 1000 * 60 * 60l; //1시간
    public UserDto join(UserJoinRequest request){

        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {throw new AppException(ErrorCode.DUPLICATED_USER_NAME,"");
                });

        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));

        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }

    public String login(UserLoginRequest request) {
        //userName 없음
        User selectedUser = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, request.getUserName() + "이 업습니다."));

        //password 틀림
        if (!encoder.matches(request.getPassword(),selectedUser.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드가 틀렸습니다.");
        }

        String token = JwtTokenUtil.createToken(selectedUser.getUserName(), key, expiredTimeMs);

        return token;
    }
}
