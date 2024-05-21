package wander.wise.application.constants;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GlobalConstants {
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    public static final String SEPARATOR = System.lineSeparator();
    public static final String RM_DIVIDER = "\\|";
    public static final String SET_DIVIDER = "|";
    public static final String TIMESTAMP_FORMAT = "dd.MM.yyyy HH:mm:ss";
}
