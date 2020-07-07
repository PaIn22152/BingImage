package com.example.bing.beans;

/**
 * Project    Bing
 * Path       com.example.bing
 * Date       2020/07/07 - 14:52
 * Author     Payne.
 * About      类描述：
 */
public class ImageBean {

    public int id;
    public String url;//地址，需要拼接
    public String dateFormat;//时间
    public String copyright;//描述和版权

    public ImageBean(String url, String dateFormat, String copyright) {
        id=-1;
        this.url = url;
        this.dateFormat = dateFormat;
        this.copyright = copyright;
    }

    public ImageBean(int id, String url, String dateFormat, String copyright) {
        this.id = id;
        this.url = url;
        this.dateFormat = dateFormat;
        this.copyright = copyright;
    }

    //    private static final String BASE_URL ="http://s.cn.bing.net";
    private static final String BASE_URL = "https://www.bing.com";

    public String getFullUrl() {
        return BASE_URL + url;
    }

}
