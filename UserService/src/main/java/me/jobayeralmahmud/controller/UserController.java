package me.jobayeralmahmud.controller;

import me.jobayeralmahmud.config.Routes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Routes.USER_SERVICE)
public class UserController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<String>> getUser(@RequestHeader("X-User-Role") String role, @RequestHeader("X-User-Permissions") List<String> permissions) {
        String hellp  = "hello";
        return ResponseEntity.ok(permissions);
    }
}
