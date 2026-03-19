package me.jobayeralmahmud.controller;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.config.Routes;
import me.jobayeralmahmud.entity.Permission;
import me.jobayeralmahmud.library.response.ApiResponse;
import me.jobayeralmahmud.repository.PermissionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Routes.AUTH_SERVICE)
@RequiredArgsConstructor
public class PermissionsSeederBaseController extends Controller {

    private final PermissionRepository repository;

    @PostMapping(Routes.SEED_PERMISSIONS)
    public ResponseEntity<ApiResponse<List<Permission>>> insertPermissions() {
        List<String> permissions = List.of("READ_USER", "EDIT_USER", "DELETE_USER");
        List<Permission> roleEntity = permissions.stream()
                .map(Permission::new)
                .toList();

        List<Permission> permissionList = repository.saveAll(roleEntity);
        return created(permissionList, "All the listed role created successfully");
    }
}
