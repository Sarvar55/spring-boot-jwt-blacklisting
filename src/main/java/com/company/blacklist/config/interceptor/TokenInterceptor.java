package com.company.blacklist.config.interceptor;

import com.company.blacklist.config.cache.TokenBlackListingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @project: blacklisting
 */
@Slf4j
@Service
@AllArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {
    private final TokenWrapper tokenWrapper;
    private final TokenBlackListingService blackListingService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String authorizationHeaderValue = request.getHeader("Authorization");

        if (StringUtils.hasText(authorizationHeaderValue) && authorizationHeaderValue.startsWith("Bearer")) {
            String token = authorizationHeaderValue.substring(7);
            String blackListedToken = blackListingService.getJwtBlackList(token);
            if (blackListedToken != null) {
                log.error("Token wos revoked");
                response.sendError(401);
                return false;
            }
            log.info("Token:{}", token);
            tokenWrapper.setToken(token);
            return true;
        }
        return false;
    }
}
