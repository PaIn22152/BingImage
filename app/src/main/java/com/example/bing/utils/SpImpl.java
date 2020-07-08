package com.example.bing.utils;

import com.afpackage.utils.kt.SpUtil;
import com.example.bing.MyApp;

/**
 * Project    Bing
 * Path       com.example.bing.utils
 * Date       2020/07/07 - 18:15
 * Author     Payne.
 * About      类描述：
 */
public class SpImpl extends SpUtil {

    private SpImpl() {
        super(MyApp.instance, "bing");
    }

    private static       SpImpl sp     = new SpImpl();
    private static final String HEADER = "spheader_";

    private static void putValue(String k, Object obj) {
        sp.put(HEADER + k, obj);
    }

    private static Object getValue(String k, Object def) {
        return sp.get(HEADER + k, def);
    }



    //当天是否获取了数据
    private static final String DAY_GETED_IMAGE_KEY = "DAY_GETED_IMAGE_KEY";

    public static void setDayGetedImage() {
        putValue(DAY_GETED_IMAGE_KEY + Convert.todayDateFormat(), true);
    }

    public static boolean getDayGetedImage() {
        return (boolean) getValue(DAY_GETED_IMAGE_KEY + Convert.todayDateFormat(), false);
    }

}
