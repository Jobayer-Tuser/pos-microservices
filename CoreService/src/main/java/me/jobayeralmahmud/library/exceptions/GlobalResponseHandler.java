package me.jobayeralmahmud.library.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.library.annotations.ApiResponseMessage;
import me.jobayeralmahmud.library.annotations.BypassGlobalResponse;
import me.jobayeralmahmud.library.response.ApiResponse;
import me.jobayeralmahmud.library.utils.Messages;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(
            MethodParameter returnType,
            @NonNull Class<? extends HttpMessageConverter<?>> converterType
    ) {

        boolean isAlreadyApiResponse = ApiResponse.class.isAssignableFrom(returnType.getParameterType());
        boolean isByPassed = returnType.hasMethodAnnotation(BypassGlobalResponse.class)
                || returnType.getContainingClass().isAnnotationPresent(BypassGlobalResponse.class);

        return !isAlreadyApiResponse &&  !isByPassed;
    }

    @Override
    public @Nullable Object beforeBodyWrite(
            @Nullable Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response
    ) {
        if (body instanceof ProblemDetail) {
            return body;
        }

        String message = Optional.ofNullable(returnType.getMethodAnnotation(ApiResponseMessage.class))
                .map(ApiResponseMessage::value)
                .map(Messages::get)
                .orElse("Success");

        var apiResponse = ApiResponse.success(body, message);

        if (body instanceof String) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return objectMapper.writeValueAsString(apiResponse);
        }

        return apiResponse;
    }
}