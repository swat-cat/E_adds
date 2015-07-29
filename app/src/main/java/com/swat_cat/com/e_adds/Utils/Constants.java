package com.swat_cat.com.e_adds.Utils;

/**
 * Created by Dell on 31.05.2015.
 */
public class Constants {
    public static String CATEGORIES_JSON_FILE = "categories.json";
    public static String DESCR_CACHE_FILE = "descr_cache.dcf";

    public static String URL_BASE = "http://e-additiv.es/api";
    public static String USER_AGENT = "Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    public static String ALL_ADDS_METHOD = "additives";
    public static String RETRIEVE_CATEGORIES_METHOD ="categories";
    public static String RETRIEVE_E_ADD_BY_CODE_METHOD = "additives";
    public static String SEARCH_METHOD="search";
    public static String E_ADD_TESS_REGEXP = "((E)(\\s?)([1-9][0-9]{2}|1[0-4][0-9]{2}|15[01][0-9]|1520)([a-z]?))";
    public static String E_CODE_VALID_REGEXP = "([1-9][0-9]{2}|1[0-4][0-9]{2}|15[01][0-9]|1520)";
    // Error Messages
    protected static final String REQUIRED_MSG = "required";
    protected static final String WRONG_CODE_MSG = "number from 100 to 1520";
}
