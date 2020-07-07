package com.example.bing;

import android.util.Log;

/**
 * Project    Bing
 * Path       com.example.bing
 * Date       2020/07/07 - 14:18
 * Author     Payne.
 * About      类描述：
 */
public class L {

    public static void d(String s) {
        d("deftag", s);
    }

    public static void e(String s) {
        e("deftag", s);
    }

    public static void d(String tag, String s) {
        Log.d(tag, s);
    }

    public static void e(String tag, String s) {
        Log.e(tag, s);
    }


}
