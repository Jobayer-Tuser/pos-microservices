package me.jobayeralmahmud.database.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.entity.Permission;
import me.jobayeralmahmud.entity.Role;
import me.jobayeralmahmud.repository.PermissionRepository;
import me.jobayeralmahmud.repository.RoleRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

//@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder /*implements CommandLineRunner*/ {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;


    @Transactional
    public void run(String... args) {
        log.info("Starting data seeding process...");

        // 1. Seed Permissions
        Permission readUser = createPermissionIfNotFound("USER_READ");
        Permission writeUser = createPermissionIfNotFound("USER_WRITE");
        Permission deleteUser = createPermissionIfNotFound("USER_DELETE");

        // 2. Seed Roles
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", Set.of(readUser, writeUser, deleteUser));
        Role userRole = createRoleIfNotFound("ROLE_USER", Set.of(readUser));

        log.info("Seeding completed successfully.");
    }

    private Permission createPermissionIfNotFound(String name) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(new Permission(name)));
    }

    private Role createRoleIfNotFound(String name, Set<Permission> permissions) {
        return roleRepository.findByName(name)
                .map(role -> {
                    role.setPermissions(permissions); // Update permissions if they changed
                    return roleRepository.save(role);
                })
                .orElseGet(() -> roleRepository.save(new Role(name)));
    }
}