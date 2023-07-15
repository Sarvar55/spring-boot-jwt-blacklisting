package com.company.blacklist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @project: blacklisting
 */
@Data
@AllArgsConstructor
public class SignInResponseDto {
    private String accessToken;
    private RefreshTokenDto refreshToken;
}
