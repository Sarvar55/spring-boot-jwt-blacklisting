package com.company.blacklist.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @project: blacklisting
 */
@Slf4j
@Service
public class JwtService {

    private String secret = "sdjfbsdflbsfkjbdkjbdvievbkejdbsjcsdbvkjsdvkhsvdbksdjbsdjfsdbfkjsdfbksjdfbds";
    private Key key;

    public JwtService() {
        byte[] keyBytes;
        keyBytes = secret.getBytes();
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String issueToken(Authentication authentication) {
        Duration duration = Duration.ofSeconds(3600);
        final JwtBuilder jwtBuilder = Jwts.builder().
                setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .claim("authorities", authorityAsStr(authentication.getAuthorities()))
                .setExpiration(Date.from(Instant.now().plus(duration)))
                .signWith(key, SignatureAlgorithm.HS512);
        return jwtBuilder.compact();
    }

    private List<String> authorityAsStr(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
