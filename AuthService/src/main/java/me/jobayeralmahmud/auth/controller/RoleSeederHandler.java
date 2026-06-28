package me.jobayeralmahmud.auth.controller;

import jakarta.validation.Validator;
import me.jobayeralmahmud.auth.entity.Role;
import me.jobayeralmahmud.auth.repository.RoleRepository;
import me.jobayeralmahmud.library.controller.BaseHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;

@Component
public class RoleSeederHandler extends BaseHandler {

    private final RoleRepository repository;

    public RoleSeederHandler(Validator validator, RoleRepository repository) {
        super(validator);
        this.repository = repository;
    }

    public ServerResponse insertRole(ServerRequest request) {
        List<String> roles = List.of("USER", "ADMIN", "EDITOR", "OPERATOR");
        List<Role> roleEntity = roles.stream()
                .filter(role -> repository.findByName(role).isEmpty())
                .map(Role::new)
                .toList();

        List<Role> roleList = repository.saveAll(roleEntity);
        return created(roleList, "All the listed role created successfully");
    }
}
