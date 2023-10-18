package com.savoirstrategie.app.config;

public class SecurityConstants {

    public static final long Token_Expiration_Time=1000000000;/**  */
    public static final String Token_Prefix="Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/register";
    public static final String TOKEN_SECRET = "dfg893hdc475zwerop4tghg4ddfdfgdsdfeqaas?=-0ljznm0-9";

    public static String getTokenSecret() {
        return TOKEN_SECRET;
    }
}