package me.jobayeralmahmud.auth.config;

public class Routes {
    public static final String EUREKA_SERVER = "/eureka";
    public static final String AUTHENTICATION = "/dev/api/v1/auth";
    public static final String LOGIN =  AUTHENTICATION + "/login";
    public static final String REGISTER = AUTHENTICATION + "/register";
    public static final String LOGOUT = AUTHENTICATION + "/logout";
    public static final String TOKEN_REFRESH = AUTHENTICATION + "/token-refresh";
}