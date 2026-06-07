package com.minimalhealth.service;

import com.minimalhealth.exception.BusinessException;
import com.minimalhealth.exception.UnauthorizedException;
import com.minimalhealth.model.dto.response.LoginResponse;
import com.minimalhealth.model.entity.User;
import com.minimalhealth.repository.UserRepository;
import com.minimalhealth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse passwordLogin(String account, String password) {
        User user = userRepository.findByPhoneOrUsername(account, account)
            .orElseThrow(() -> new UnauthorizedException("账号或密码错误"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new UnauthorizedException("账号或密码错误");
        }

        return buildLoginResponse(user);
    }

    public LoginResponse register(String phone, String password) {
        if (userRepository.existsByPhone(phone)) {
            throw new BusinessException("该手机号已注册");
        }

        User user = User.builder()
            .phone(phone)
            .passwordHash(passwordEncoder.encode(password))
            .profileComplete(false)
            .build();
        userRepository.save(user);

        log.info("新用户注册: phone={}", phone);
        return buildLoginResponse(user);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("登录已过期，请重新登录");
        }

        Long userId = tokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UnauthorizedException("用户不存在"));

        String newAccessToken = tokenProvider.generateAccessToken(userId);
        String newRefreshToken = tokenProvider.generateRefreshToken(userId);

        return LoginResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .user(buildUserInfo(user))
            .build();
    }

    private LoginResponse buildLoginResponse(User user) {
        String accessToken = tokenProvider.generateAccessToken(user.getId());
        String refreshToken = tokenProvider.generateRefreshToken(user.getId());

        return LoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(buildUserInfo(user))
            .build();
    }

    private LoginResponse.UserInfo buildUserInfo(User user) {
        return LoginResponse.UserInfo.builder()
            .id(user.getId())
            .name(user.getName())
            .phone(user.getPhone())
            .profileComplete(user.getProfileComplete())
            .build();
    }
}
