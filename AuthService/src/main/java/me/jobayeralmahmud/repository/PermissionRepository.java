package me.jobayeralmahmud.repository;

import me.jobayeralmahmud.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
