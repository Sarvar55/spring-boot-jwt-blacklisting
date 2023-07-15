package com.company.blacklist.security.repository;

import com.company.blacklist.security.domain.AuthUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

/**
 * @project: blacklisting
 */
@Repository
public interface UserRepository extends JpaRepository<AuthUser, Long> {
    @EntityGraph(attributePaths = {"authorities"})
    UserDetails findByUsername(String username);
}
