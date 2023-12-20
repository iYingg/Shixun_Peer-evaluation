package com.zjt.server.util;

public class Allocate {
    public static String allocate(String str){
        String[] strs = str.split(",");
        String result = "";
        for (int i = 0; i < strs.length; i++) {
            if (i == strs.length - 1){
                result += strs[i];
            }else {
                result += strs[i] + ",";
            }
        }
        return result;
    }
}
