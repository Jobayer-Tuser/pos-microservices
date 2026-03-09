package me.jobayeralmahmud.controller;

import me.jobayeralmahmud.config.Routes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Routes.USER_SERVICE)
public class UserController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> getUser(){
        String hellp  = "hello";
        return ResponseEntity.ok(hellp);
    }
}
