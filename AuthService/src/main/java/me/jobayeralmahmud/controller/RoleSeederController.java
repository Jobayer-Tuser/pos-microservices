package me.jobayeralmahmud.controller;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.config.Routes;
import me.jobayeralmahmud.dto.response.ApiResponse;
import me.jobayeralmahmud.entity.Role;
import me.jobayeralmahmud.repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Routes.AUTH_SERVICE)
@RequiredArgsConstructor
public class RoleSeederController extends Controller {

    private final RoleRepository repository;

    @PostMapping(Routes.SEED_ROLES)
    public ResponseEntity<ApiResponse<List<Role>>> insertRole() {
        List<String> roles = List.of("USER", "ADMIN", "EDITOR", "OPERATOR");
        List<Role> roleEntity = roles.stream()
                .map(Role::new)
                .toList();

        List<Role> roleList = repository.saveAll(roleEntity);
        return created(roleList, "All the listed role created successfully");
    }
}
