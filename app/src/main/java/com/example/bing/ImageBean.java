package com.example.bing;

/**
 * Project    Bing
 * Path       com.example.bing
 * Date       2020/07/07 - 14:52
 * Author     Payne.
 * About      类描述：
 */
public class ImageBean {

    public String url;
    public String dateFormat;
    public String copyright;

    public ImageBean(String url, String dateFormat, String copyright) {
        this.url = url;
        this.dateFormat = dateFormat;
        this.copyright = copyright;
    }

//    public static final String BASE_URL ="http://s.cn.bing.net";
    public static final String BASE_URL="https://www.bing.com";

}
