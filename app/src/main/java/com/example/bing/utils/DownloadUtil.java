package com.example.bing.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.afpackage.utils.kt.LogUtilKt;
import com.example.bing.beans.ImageBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Project    Bing
 * Path       com.example.bing
 * Date       2020/07/07 - 14:14
 * Author     Payne.
 * About      类描述：
 */
public class DownloadUtil {

    private static DownloadUtil downloadUtil;
    private final  OkHttpClient okHttpClient;
    private        Handler      handler;

    private Map<String, OnDownloadListener> downloadingMap = new HashMap<>();

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    private boolean addUrl(String url, OnDownloadListener listener) {
        boolean have = downloadingMap.containsKey(url);
        downloadingMap.put(url, listener);
        return have;
    }

    private void removeUrl(String url) {
        if (downloadingMap.containsKey(url)) {
            downloadingMap.remove(url);
        }
    }

    private void d(String s) {
        LogUtilKt.logD(s);
        LogUtilKt.logD("apitag", s);
    }

    /**
     * @param url              下载连接
     * @param downloadListener 下载监听
     */
    public void download(Context context, ImageBean imageBean,
            final OnDownloadListener downloadListener) {
        boolean have = addUrl(imageBean.getFullUrl(), downloadListener);
        if (have) {
            return;
        }
        String url = imageBean.getFullUrl();
        String localPath = PathUtil.getLocalPath(imageBean.url);

        File file = new File(localPath);
        if (file.exists() && file.length() > 10) {
            if (downloadListener != null) {
                downloadListener.onDownloaded(localPath);
            }
            return;
        }

        String cachePath = PathUtil.getCachePath(imageBean.url);

        FileUtil.createFile(cachePath);
        FileUtil.createFile(localPath);

        Request request = new Request.Builder().url(url).build();
//        long downloadLength = 0, contentLength = getContentLength(url);
//        request.header()
//        request.header("RANGE", "bytes=" + downloadLength + "-" + contentLength);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                d("download  onFailure e = " + e);
                // 下载失败
                handler.post(() -> {
                    OnDownloadListener listener = downloadingMap.get(url);
                    if (listener != null) {
                        listener.onDownloadFailed(" e1 = " + e.toString());
                    }
                    removeUrl(url);
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                d("download  onResponse  Thread = " + Thread.currentThread());
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
//                String savePath = isExistDir(videoCachePath);
                String savePath = cachePath;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
//                    File file = new File(savePath, getNameFromUrl(url));
//                    File file = new File(savePath+getNameFromUrl(url));
                    File file = new File(savePath);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        handler.post(() -> {
                            OnDownloadListener listener = downloadingMap.get(url);
                            if (listener != null) {
                                listener.onDownloading(progress);
                            }
                        });

                    }
                    fos.flush();
                    // 下载完成

                    boolean move = FileUtil.move(cachePath, localPath);
                    if (move) {
                        handler.post(() -> {
                            OnDownloadListener listener = downloadingMap.get(url);
                            if (listener != null) {
                                listener.onDownloadSuccess(localPath);
                            }
                            removeUrl(url);
                        });
                    } else {
                        handler.post(() -> {
                            OnDownloadListener listener = downloadingMap.get(url);
                            if (listener != null) {
                                listener.onDownloadFailed(" move = " + move);
                            }
                            removeUrl(url);
                        });

                    }

                } catch (Exception e) {
                    d("");
                    handler.post(() -> {
                        OnDownloadListener listener = downloadingMap.get(url);
                        if (listener != null) {
                            listener.onDownloadFailed(" e2 = " + e.toString());
                        }
                        removeUrl(url);
                    });

                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        });
    }


    /**
     * 获取下载长度
     *
     * @param downloadUrl
     * @return
     */
    private long getContentLength(String downloadUrl) {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();
//                return contentLength == 0 ? DownloadInfo.TOTAL_ERROR : contentLength;
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return DownloadInfo.TOTAL_ERROR;
        return 0;
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {

        /**
         * 已经下载到本地了
         */
        void onDownloaded(String localPath);

        /**
         * 下载成功
         */
        void onDownloadSuccess(String localPath);

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed(String err);
    }


}
