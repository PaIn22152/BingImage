package com.example.bing.utils;

import android.content.Context;
import android.os.Environment;
import com.afpackage.utils.kt.Md5Kt;
import com.example.bing.MyApp;

/**
 * Project    Bing
 * Path       com.example.bing.utils
 * Date       2020/07/07 - 18:30
 * Author     Payne.
 * About      类描述：
 */
public class PathUtil {

    private static final String APP_BASE_PATH = MyApp.instance.getFilesDir().getAbsolutePath();
//    private static final String SD_BASE_PATH  = Environment.getExternalStorageDirectory().getPath();
    private static final String SD_BASE_PATH  = MyApp.instance.getExternalFilesDir("image").getPath();


    ////th?id=OHR.Kamchatka_ZH-CN8647931935_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp
    public static String getLocalPath(String url) {
        String tail = url.contains(".jpg") ? ".jpg" : (url.contains(".png") ? ".png" : "");
        String md5 = Md5Kt.MD5Encrypt(url);
        return SD_BASE_PATH + "/bing/" + md5 + tail;

    }

    public static String getCachePath(String url) {
        String md5 = Md5Kt.MD5Encrypt(url);
        return SD_BASE_PATH + "/cache/" + md5;
    }

}
