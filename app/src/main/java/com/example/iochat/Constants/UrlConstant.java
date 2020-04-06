package com.example.iochat.Constants;

public class UrlConstant {

    static {
        IOUrl = "http://192.168.1.104:3000/";
        IOConnection = "connection";
    }

    private static String IOUrl;

    private static String IOConnection;

    public static String getIOConnection() {
        return IOConnection;
    }

    public static String getIOUrl() {
        return IOUrl;
    }
}
