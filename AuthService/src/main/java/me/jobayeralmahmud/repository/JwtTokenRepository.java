package me.jobayeralmahmud.repository;

import me.jobayeralmahmud.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {


    @Query("""
        SELECT jt FROM JwtToken jt inner join jt.user u
            on jt.user.id = u.id
        WHERE jt.user.id = :userId AND jt.isLoggedOut = false
    """)
    List<JwtToken> findAllJwtTokenByUser(UUID userId);

    Optional <JwtToken> findByToken(String token);
}
