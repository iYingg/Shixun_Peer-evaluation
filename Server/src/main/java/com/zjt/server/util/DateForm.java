package com.zjt.server.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateForm {
    public static String DatetoString(Date date){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        return sdf1.format(date);
    }


    public static Date StringtoDate(String str){
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = null;
        try {
            myDate = sdf2.parse(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return myDate;
    }
}
