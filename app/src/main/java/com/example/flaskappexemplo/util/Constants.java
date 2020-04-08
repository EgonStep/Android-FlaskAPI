package com.example.flaskappexemplo.util;

public class Constants {

    public Constants() { /* noop */ }

    // Android Studio can access the localhost IP by using this address http://10.0.2.2
    public static final String URI_CONSOLE = "http://10.0.2.2:5000/api/consoles";
    public static final String CREATED_CONSOLE = "New Console Created - id: ";
    public static final String UPDATED_CONSOLE = "Console updated - id: ";
    public static final String DELETE_CONSOLE = "Console deleted - id: ";
    public static final String DELETE = "Delete";
    public static final String DELETE_WARN = "Delete console with id ";

    public static final String[] IS_ACTIVE_FLAG = {"Active Console", "Not Active Console"};
}
