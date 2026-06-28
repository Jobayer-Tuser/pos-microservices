package me.jobayeralmahmud.auth.controller;

import jakarta.validation.Validator;
import me.jobayeralmahmud.auth.entity.Permission;
import me.jobayeralmahmud.auth.repository.PermissionRepository;
import me.jobayeralmahmud.library.controller.BaseHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;

@Component
public class PermissionSeederHandler extends BaseHandler {

    private final PermissionRepository repository;

    public PermissionSeederHandler(Validator validator, PermissionRepository repository) {
        super(validator);
        this.repository = repository;
    }

    public ServerResponse insertPermissions(ServerRequest request) {
        List<String> permissions = List.of("READ_USER", "EDIT_USER", "DELETE_USER");
        List<Permission> roleEntity = permissions.stream()
                .filter(permission -> repository.findByName(permission).isEmpty())
                .map(Permission::new)
                .toList();

        List<Permission> permissionList = repository.saveAll(roleEntity);
        return created(permissionList, "All the listed role created successfully");
    }
}
