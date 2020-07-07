package com.example.bing.background.events;

import com.example.bing.beans.ImageBean;
import java.util.List;

/**
 * Project    Bing
 * Path       com.example.bing
 * Date       2020/07/07 - 15:01
 * Author     Payne.
 * About      类描述：
 */
public class ImageEvent {
   public List<ImageBean> list;

    public ImageEvent(List<ImageBean> list) {
        this.list = list;
    }
}
