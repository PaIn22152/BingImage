package com.example.bing.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Project    Bing
 * Path       com.example.bing.utils
 * Date       2020/07/07 - 18:41
 * Author     Payne.
 * About      类描述：
 */
public class Convert {

    public static String dateFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date());
    }

}
