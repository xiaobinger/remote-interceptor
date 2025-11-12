package com.xiaobinger.tools.interceptor.core;

/**
 * @author xiongbing
 * @date 2025/11/12 9:14
 * @description
 */
public class CommonUtils {


    protected static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty() && !"null".equals(str.trim());
    }

    protected static boolean containsAny(String str, String... searchStrs) {
        if (str == null || searchStrs == null) {
            return false;
        }
        for (String searchStr : searchStrs) {
            if (str.contains(searchStr)) {
                return true;
            }
        }
        return false;
    }



}
