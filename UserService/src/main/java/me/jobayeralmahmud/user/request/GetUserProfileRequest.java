package me.jobayeralmahmud.user.request;

public record GetUserProfileRequest(
        String property,
        Long lastId,
        Integer pageSize
) {

    public GetUserProfileRequest {
        property = (property == null) ? "id" : property;
        pageSize = (pageSize == null) ? 10 : pageSize;
    }
}
