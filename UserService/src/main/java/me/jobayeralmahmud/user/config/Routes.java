package me.jobayeralmahmud.user.config;

public class Routes {

    public static final String TEST         = "/test";
    public static final String VERSION      = "/v1";
    public static final String RELEASE      = "/prod";
    public static final String DEVELOPER    = "/dev";

    public static final String USER_SERVICE = DEVELOPER + "/api" + VERSION +"/user";

    public static final String REGISTER = "/register";
    public static final String AUTH_SERVICE = DEVELOPER + "/api" + VERSION + "/auth";
    public static final String UPDATE_USER_PROFILE = "/update-profile";

}