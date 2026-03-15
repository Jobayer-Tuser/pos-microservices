package me.jobayeralmahmud.config;

public class Routes {

    public static final String TEST = "/test";
    public static final String RELEASE = "/prod";
    public static final String DEVELOPER = "/dev";
    public static final String VERSION = "/v1";

    public static final String LOGIN =  "/login";
    public static final String LOGOUT = "/logout";
    public static final String REGISTER = "/register";
    public static final String SEED_ROLES = "/seed-roles";
    public static final String TOKEN_REFRESH = "/token-refresh";
    public static final String VALIDATED_PROFILE = "/validated/profile";
    public static final String SEED_PERMISSIONS = "/seed-permissions";
    public static final String SEED_ROLES_PERMISSIONS = "/seed-roles-permissions";
    public static final String AUTH_SERVICE = DEVELOPER + "/api" + VERSION + "/auth";

}