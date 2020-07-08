package com.example.bing.utils;

import android.widget.Toast;
import com.example.bing.MyApp;

/**
 * Project    Bing
 * Path       com.example.bing.utils
 * Date       2020/07/08 - 10:29
 * Author     Payne.
 * About      类描述：
 */
public class ToastUtil {
    public static void showLong(String s){
        Toast.makeText(MyApp.instance,s,Toast.LENGTH_LONG).show();
    }

}
