package me.jobayeralmahmud.user.controller;

import me.jobayeralmahmud.library.response.ApiResponse;
import me.jobayeralmahmud.user.request.CreateUserRequest;
import me.jobayeralmahmud.user.response.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "http://auth-service:9013/dev/api/v1/auth")
public interface AuthClient {

    @PostMapping("/register")
    ApiResponse<UserDto> createAccount(@RequestBody CreateUserRequest request);
}