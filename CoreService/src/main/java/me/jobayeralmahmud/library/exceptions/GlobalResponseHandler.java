package me.jobayeralmahmud.library.exceptions;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.annotations.ApiResponseMessage;
import me.jobayeralmahmud.library.annotations.BypassGlobalResponse;
import me.jobayeralmahmud.library.response.ApiResponse;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tools.jackson.databind.ObjectMapper;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(
            MethodParameter returnType,
            @NonNull Class<? extends HttpMessageConverter<?>> converterType
    ) {
        // Skip wrapping if the endpoint already returns an ApiResponse or raw String/ProblemDetail
        var clazz = returnType.getParameterType();
        boolean hasMethodAnnotation = returnType.hasMethodAnnotation(BypassGlobalResponse.class);
        boolean hasClassAnnotation = returnType.getContainingClass().isAnnotationPresent(BypassGlobalResponse.class);

        return !(ApiResponse.class.isAssignableFrom(clazz)|| hasMethodAnnotation || hasClassAnnotation);
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
        var messageAnnotation = returnType.getMethodAnnotation(ApiResponseMessage.class);
        String message = messageAnnotation != null ? messageAnnotation.value() : "Success";
        
        if (body == null) {
            return ApiResponse.success(null, message);
        }

        if (body instanceof String) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(ApiResponse.success(body, message));
            } catch (Exception e) {
                throw new RuntimeException("Error processing string response wrapper", e);
            }

        }
        return ApiResponse.success(body, message);
    }
}