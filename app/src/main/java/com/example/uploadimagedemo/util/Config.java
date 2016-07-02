package com.example.uploadimagedemo.util;

import android.os.Environment;

public class Config {

    public static String TAG = "AddMessage";
    public static String DB_NAME = "AddMessage.db";
    // Create a directory in SD CARD
    public static String APP_HOME = Environment.getExternalStorageDirectory()
            .getPath() + "/" + TAG;

    // A directory to store logs
    public static String DIR_LOG = APP_HOME + "/log";
    // preference file name
    public static final String PREF_FILE = TAG + "_PREF";

    public static String DIR_USERDATA = APP_HOME + "/userdata";

    /* API */
    public static String API_KEY = "mydata.upload";
    public static String API_VERSION = "v1";

    public static String HOST = "http://example.com.upload/apis";// FINAL
    public static String API_ADD_PROPERTY_BASIC_DETAIL = HOST
            + "/properties/saveproperty/";
    public static String TAG_ADD_PROPERTY_BASIC_DETAIL = "API_ADD_PROPERTY_BASIC_DETAIL";

    /*
     * Error and Warnings
     */
    public static String ERROR_NETWORK = "ERROR_NETWORK";
    public static String ERROR_API = "ERROR_API";
    public static String ERROR_UNKNOWN = "ERROR_UNKNOWN";

    public static int API_SUCCESS = 0;
    public static int API_FAIL = 1;

    // connection timeout is set to 20 seconds
    public static int TIMEOUT_CONNECTION = 20000;

    // SOCKET TIMEOUT IS SET TO 30 SECONDS
    public static int TIMEOUT_SOCKET = 30000;

    /* PREFERENCE VARIABLES */
    public static String PREF_USERID = "PREF_USERID";
    public static String PREF_BK_TIMESTAMP = "PREF_BK_TIMESTAMP";
    public static String PREF_TOTAL_CATEGORY = "PREF_TOTAL_CATEGORY";
    public static String PREF_TOTAL_MESSAGE = "PREF_TOTAL_MESSAGE";

    /*
     * Cookie and SESSION
     */
    public static String PREF_SESSION_COOKIE = "sessionid";
    public static String SET_COOKIE_KEY = "Set-Cookie";
    public static String COOKIE_KEY = "Cookie";
    public static String SESSION_COOKIE = "sessionid";

}
