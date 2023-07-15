package com.company.blacklist.config.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @project: blacklisting
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TokenBlackListingService {

    private final CacheManager cacheManager;
    private static final String CACHE_NAME = "jwt-token";

    @CachePut(cacheNames = {CACHE_NAME}, key = "#token")
    public String blackListJwt(String token) {
        log.info("Token for revoking: {}", token);
        return token;
    }

    @Cacheable(cacheNames = {CACHE_NAME}, unless = "#result == null")
    public String getJwtBlackList(String token) {
        return null;
    }

    @CacheEvict(CACHE_NAME)
    public void clearCache() {
    }


}
