package me.jobayeralmahmud.store.repository;

import me.jobayeralmahmud.store.entity.Store;
import me.jobayeralmahmud.store.response.StoreDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("""
            SELECT s.id as id,
                     s.ownerId as owner_id,
                     s.brandName as name,
                     s.phoneNumber as phoneNumber,
                     s.status as status
            FROM Store s
                ORDER BY s.id ASC
            """)
    Slice<StoreDto> findAllStore(Pageable pageable);

    Optional<Store> findById(UUID id);
    Optional<Store> findByOwnerId(UUID ownerId);
}