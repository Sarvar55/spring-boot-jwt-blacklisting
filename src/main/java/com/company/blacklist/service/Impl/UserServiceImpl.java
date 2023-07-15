package com.company.blacklist.service.Impl;

import com.company.blacklist.dto.RefreshTokenDto;
import com.company.blacklist.dto.SignInRequestDto;
import com.company.blacklist.dto.SignInResponseDto;
import com.company.blacklist.security.domain.RefreshToken;
import com.company.blacklist.security.repository.RefreshTokenRepository;
import com.company.blacklist.security.service.JwtService;
import com.company.blacklist.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @project: blacklisting
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public SignInResponseDto signIn(SignInRequestDto requestDto) {
        log.info("Authenticating user {}", requestDto.getUsername());
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(requestDto.getUsername(),
                requestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new SignInResponseDto(jwtService.issueToken(authentication), issueRefreshToken(authentication, null));
    }

    @Override
    public SignInResponseDto authWithRefreshToken(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenDto.getToken())
                .orElseThrow(() -> new RuntimeException("Unauthorize"));
        if (refreshToken.isValid() && refreshToken.getEat().after(new Date())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.getUsername());
            if (userDetails.isAccountNonExpired()
                    && userDetails.isEnabled()
                    && userDetails.isAccountNonLocked()
                    && userDetails.isCredentialsNonExpired()) {
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                        userDetails.getPassword());
                refreshToken.setValid(false);
                refreshTokenRepository.save(refreshToken);
                return new SignInResponseDto(jwtService.issueToken(authenticationToken), issueRefreshToken(authenticationToken, refreshToken.getId()));
            }
        }throw new RuntimeException();
    }

    private RefreshTokenDto issueRefreshToken(Authentication authentication, Long prevRefreshTokenId) {
        Calendar date = Calendar.getInstance();
        Long timeInSec = date.getTimeInMillis();
        Date afterAdd10Mins = new Date(timeInSec + (180L * 24 * 3_600__00));
        RefreshToken refreshToken = RefreshToken.builder()
                .username(authentication.getName())
                .token(UUID.randomUUID().toString())
                .eat(afterAdd10Mins)
                .valid(true)
                .previousRefreshTokenId(prevRefreshTokenId)
                .build();
        refreshTokenRepository.save(refreshToken);
        return new RefreshTokenDto(refreshToken.getToken(), refreshToken.getEat());

    }
}
