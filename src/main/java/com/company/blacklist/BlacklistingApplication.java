package com.company.blacklist;

import com.company.blacklist.security.domain.AuthUser;
import com.company.blacklist.security.domain.Authority;
import com.company.blacklist.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class BlacklistingApplication implements CommandLineRunner {

    private final PasswordEncoder encoder;
    private final UserRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(BlacklistingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        AuthUser authUser =new AuthUser();
        authUser.setUsername("user");
        authUser.setPassword(encoder.encode("test"));

        Authority adminRole = new Authority();
        adminRole.setAuthority("ROLE_ADMIN");
        Authority userRole = new Authority();
        userRole.setAuthority("ROLE_USER");

        authUser.setAuthorities(List.of(adminRole, userRole));
        repository.save(authUser);
    }
}
