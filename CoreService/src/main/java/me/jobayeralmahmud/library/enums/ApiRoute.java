package me.jobayeralmahmud.library.enums;

public enum ApiRoute {

    TEST("/test"),
    VERSION("/v1"),
    RELEASE("/release"),
    DEVELOPER("/dev"),

    DEV_AUTH_PREFIX(DEVELOPER.path + VERSION.path + "/auth"),
    TEST_AUTH_PREFIX(TEST.path + VERSION.path + "/auth"),
    RELEASE_AUTH_PREFIX(RELEASE.path + VERSION.path + "/auth"),

    REGISTER(DEV_AUTH_PREFIX.path + "/register"),
    LOGIN(DEV_AUTH_PREFIX.path + "/login");

    private final String path;

    ApiRoute(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    /*
    private static class RouteConstants {
        private static final String DEVELOPER = "/dev";
        private static final String VERSION = "/v1";
        private static final String AUTH_PREFIX = DEVELOPER + VERSION + "/auth";
    }*/
}

