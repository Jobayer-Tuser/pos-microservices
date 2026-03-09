package me.jobayeralmahmud.repository;

import me.jobayeralmahmud.entity.User;
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

    @EntityGraph(attributePaths = "role")
    Optional<User> findByEmail(String email);

    @EntityGraph(value = "graph.userRole")
    Optional<User> findUserById(UUID id);
    Boolean existsByEmail(String email);

    @Query("""
            SELECT u FROM User u 
                LEFT JOIN FETCH u.role 
            WHERE (:cursor IS NULL OR u.id > :cursor) 
            ORDER BY u.id ASC 
    """)
    List<User> cursorPaginationPattern(@Param("cursor") Long cursor, Pageable pageable);

    @Query("select u from User u join fetch u.role r join fetch r.permissions where u.email = :email")
    Optional<User> findByEmailWithPermissions(@Param("email") String email);
}
