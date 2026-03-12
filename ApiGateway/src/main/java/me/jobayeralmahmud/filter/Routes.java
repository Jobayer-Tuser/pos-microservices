package me.jobayeralmahmud.filter;

public class Routes {

    public static final String EUREKA_SERVER = "/eureka";
    public static final String AUTHENTICATION = "/dev/api/v1/auth";
    public static final String LOGIN =  AUTHENTICATION.concat("/login");
    public static final String REGISTER = AUTHENTICATION.concat("/REGISTER");
    public static final String TOKEN_REFRESH = AUTHENTICATION.concat("/token-refresh");
}