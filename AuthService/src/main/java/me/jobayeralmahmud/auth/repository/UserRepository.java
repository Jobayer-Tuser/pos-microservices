package me.jobayeralmahmud.auth.repository;

import me.jobayeralmahmud.auth.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "role")
    Optional<User> findByEmail(String email);

    @EntityGraph(value = "graph.userRole")
    Optional<User> findUserById(UUID id);

    @Query("""
            SELECT u FROM User u
                LEFT JOIN FETCH u.role
            WHERE (:cursor IS NULL OR u.id > :cursor)
            ORDER BY u.id ASC
    """)
    List<User> cursorPaginationPattern(@Param("cursor") Long cursor, Pageable pageable);

    @Query("""
        select u from User u
            left join fetch u.role r
                left join fetch r.permissions
                    where u.email = :email and u.emailVerifiedAt is not null
    """)
    Optional<User> findByEmailWithRoleAndPermissions(@Param("email") String email);

    @Query("""
        SELECT u.emailVerifiedAt IS NOT NULL
            FROM User u
                WHERE u.id = :userId
    """)
    Optional<Boolean> isEmailVerified(@Param("id") UUID userId);
}