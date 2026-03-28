package me.jobayeralmahmud.auth.config;

public final class Routes {

    private Routes() {}

    public static final String VERSION = "/v1";
    public static final String DEV_API = "/dev/api" + VERSION;

    public static final class Auth {
        public static final String BASE = DEV_API + "/auth";

        public static final String LOGIN = "/login";
        public static final String LOGOUT = "/logout";
        public static final String REGISTER = "/register";
        public static final String TOKEN_REFRESH = "/token-refresh";

        // Full paths for internal logic if needed
        public static final String FULL_LOGIN = BASE + LOGIN;
        public static final String FULL_LOGOUT = BASE + LOGOUT;
        public static final String FULL_REGISTER = BASE + REGISTER;
        public static final String FULL_TOKEN_REFRESH = BASE + TOKEN_REFRESH;
    }

    public static final class Seed {
        public static final String BASE = DEV_API + "/seed";

        public static final String ROLES = "/roles";
        public static final String PERMISSIONS = "/permissions";
        public static final String ROLES_PERMISSIONS = "/roles-permissions";
    }

    public static final class User {
        public static final String BASE = DEV_API + "/user";
        public static final String VALIDATED_PROFILE = "/validated/profile";
    }

}