package com.bootdo.ext.util;

import org.apache.http.HttpHeaders;

/**
 * @author 249875211@qq.com
 */
public interface IConstants {
    /**
     * map key,流程id的前缀
     */
    final static String PROCESS_VARS_PID_PREFIX = "PCID";
    /**
     * map key, 流程id，流程名字分隔符
     */
    final static String SEP_PROCESS_PID_NAME = "_";

    final static String PROCESS_VAR_PARA_PREFIX = "$";
    final static String PROCESS_VAR_PARA_SEP = "_";


    final static String ERROR_CODE_KEY = "error_code";


    final static String KEY_MAP_TOKEN = HttpHeaders.AUTHORIZATION;
    final static String KEY_MAP_TOKEN_02 = "access_token";

    final static String KEY_MAP_PROCESS_ERROR_CODE = "key_map_process_error_code";
    final static String KEY_MAP_PROCESS_ERROR_PROCESS = "KEY_MAP_PROCESS_ERROR_PROCESS";
    final static String KEY_MAP_GLOBAL_FLOWABLE_VARS = "KEY_MAP_GLOBAL_FLOWABLE_VARS";
    final static String KEY_MAP_GLOBAL_FLOW_RESPONSE = "key_map_global_flow_response";
    final static String KEY_MAP_STATUS_CODE_HTTP = "httpcode";
    final static String KEY_MAP_STATUS_REASON_HTTP = "httpreason";
    final static String KEY_MAP_STATUS_CODE_SQL = "sqlcode";
    final static String KEY_MAP_STATUS_CODE_SHELL = "shellcode";
}
