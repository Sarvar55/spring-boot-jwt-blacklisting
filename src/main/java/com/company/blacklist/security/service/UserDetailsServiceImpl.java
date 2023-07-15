package com.company.blacklist.security.service;

import com.company.blacklist.config.cache.TokenBlackListingService;
import com.company.blacklist.config.interceptor.TokenWrapper;
import com.company.blacklist.dto.UserCredentialChange;
import com.company.blacklist.security.domain.AuthUser;
import com.company.blacklist.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @project: blacklisting
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlackListingService blackListingService;
    private final TokenWrapper tokenWrapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public void changePassword(UserCredentialChange credentialChange) {
        AuthUser authUser = (AuthUser) loadUserByUsername(credentialChange.username());

        if (passwordEncoder.matches(credentialChange.oldPassword(), authUser.getPassword())) {
            authUser.setPassword(passwordEncoder.encode(credentialChange.newPassword()));
            blackListingService.blackListJwt(tokenWrapper.getToken());
            userRepository.save(authUser);
        } else throw new IllegalArgumentException("Bad credential");
    }
}
