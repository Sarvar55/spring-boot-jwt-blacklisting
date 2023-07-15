package com.company.blacklist.controller;

/**
 * @project: blacklisting
 */

import com.company.blacklist.config.cache.TokenBlackListingService;
import com.company.blacklist.config.interceptor.TokenWrapper;
import com.company.blacklist.dto.RefreshTokenDto;
import com.company.blacklist.dto.SignInRequestDto;
import com.company.blacklist.dto.SignInResponseDto;
import com.company.blacklist.dto.UserCredentialChange;
import com.company.blacklist.security.service.UserDetailsServiceImpl;
import com.company.blacklist.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailService;

    private final TokenBlackListingService blackListingService;
    private final TokenWrapper tokenWrapper;

    @PutMapping("/auth/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody @Valid UserCredentialChange userCredential) {
        userDetailService.changePassword(userCredential);
    }

    @PostMapping("/auth/sign-in")
    public SignInResponseDto signIn(@RequestBody @Valid SignInRequestDto requestDto) {
        return userService.signIn(requestDto);
    }

    @PostMapping("/auth/refresh-token")
    public SignInResponseDto signIn(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        return userService.authWithRefreshToken(refreshTokenDto);
    }
}

