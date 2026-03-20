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
        SELECT t FROM JwtToken t inner join t.user u
            on t.user.id = u.id
        WHERE t.user.id = :userId AND t.isLoggedOut = false
    """)
    List<JwtToken> findAllJwtTokenByUser(UUID userId);

    Optional <JwtToken> findByToken(String token);
}
