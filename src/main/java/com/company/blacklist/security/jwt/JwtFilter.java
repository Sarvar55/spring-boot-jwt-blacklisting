package com.company.blacklist.security.jwt;

import com.company.blacklist.security.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @project: blacklisting
 */
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final String AUTH_HEADER = "Authorization";
    private final String BEARER_AUTH_HEADER = "Bearer";
    private final String TOKEN_AUTHORITIES = "authorities";
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Optional<Authentication> authentication = getAuthentication(request);
        if (authentication.isPresent()) {
            SecurityContextHolder.getContext().setAuthentication(authentication.get());
        } else {
            logger.info("NO Bearer Authentication");
        }
        filterChain.doFilter(request, response);
    }


    public Optional<Authentication> getAuthentication(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTH_HEADER))
                .filter(this::isAuthBearer)
                .flatMap(this::getAuthenticationHeader);

    }

    private boolean isAuthBearer(String header) {
        return header.toLowerCase().startsWith(BEARER_AUTH_HEADER.toLowerCase());
    }

    private Optional<Authentication> getAuthenticationHeader(String header) {
        String token = header.substring(BEARER_AUTH_HEADER.length()).trim();
        Claims claims = jwtService.parseToken(token);
        logger.info("Calims:{}" + claims.toString());
        return Optional.of(getAuthenticationBearer(claims));
    }

    private Authentication getAuthenticationBearer(Claims claims) {
        List<?> authorites = claims.get(TOKEN_AUTHORITIES, List.class);
        logger.info("Authoriteis" + authorites.toString());
        List<GrantedAuthority> grantedAuthorities;
        grantedAuthorities = authorites.stream()
                .map(o -> new SimpleGrantedAuthority(o.toString()))
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", grantedAuthorities);
    }

}
