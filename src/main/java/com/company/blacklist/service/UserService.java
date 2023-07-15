package com.company.blacklist.service;

import com.company.blacklist.dto.RefreshTokenDto;
import com.company.blacklist.dto.SignInRequestDto;
import com.company.blacklist.dto.SignInResponseDto;

/**
 * @project: blacklisting
 */
public interface UserService {
    SignInResponseDto signIn(SignInRequestDto requestDto);
    SignInResponseDto authWithRefreshToken(RefreshTokenDto refreshTokenDto);
}
