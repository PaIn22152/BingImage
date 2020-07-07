package com.example.bing;

import com.afpackage.utils.BaseApp;

/**
 * Project    Bing
 * Path       com.example.bing
 * Date       2020/07/07 - 17:51
 * Author     Payne.
 * About      类描述：
 */
public class MyApp extends BaseApp {

    public static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
