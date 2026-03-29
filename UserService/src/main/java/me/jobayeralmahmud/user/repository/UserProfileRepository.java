package me.jobayeralmahmud.user.repository;

import me.jobayeralmahmud.user.entity.UserProfile;
import me.jobayeralmahmud.user.response.UserProfileSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(UUID userId);

    @Query("""
        select
            p.id as id,
            p.userId as userId,
            p.age as age,
            p.firstName as firstName,
            p.lastName as lastName,
            p.displayName as displayName,
            p.phoneNumber as phoneNumber,
            p.permanentAddress as permanentAddress,
            p.gender as gender
        from UserProfile p where ( :lastId is null or p.id > :lastId )
    """)
    Slice<UserProfileSummary> fetchNextPage(@Param("lastId") Long lastId, Pageable pageable);
}