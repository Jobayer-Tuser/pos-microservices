package me.jobayeralmahmud.user.repository;

import me.jobayeralmahmud.user.entity.UserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(UUID userId);

    @Query("select p from UserProfile p where ( :lastId is null or p.id > :lastId )")
    List<UserProfile> fetchNextPage(@Param("lastId") Long lastId, Pageable pageable);
}
