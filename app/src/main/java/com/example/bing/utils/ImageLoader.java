package com.example.bing.utils;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.bing.beans.ImageBean;
import java.io.File;
import java.nio.file.Path;

/**
 * Project    Bing
 * Path       com.example.bing.utils
 * Date       2020/07/07 - 18:38
 * Author     Payne.
 * About      类描述：
 */
public class ImageLoader {

    public static void load(Context context, ImageView imageView, ImageBean bean) {
        String localPath = PathUtil.getLocalPath(bean.url);
        File file = new File(localPath);
        if (file.exists() && file.length() > 10) {
            Glide.with(context).load(localPath).into(imageView);
        } else {
            Glide.with(context).load(bean.getFullUrl()).into(imageView);
        }
    }

}
