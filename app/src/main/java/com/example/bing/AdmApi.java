package com.example.bing;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.afpackage.utils.kt.LogUtilKt;
import com.afpackage.utils.kt.ThreadPool;
import com.example.bing.DownloadUtil.OnDownloadListener;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Project    Bing
 * Path       com.example.bing
 * Date       2020/07/07 - 14:42
 * Author     Payne.
 * About      类描述：
 */
public class AdmApi {


    private static AdmApi helper;


    public static AdmApi getHelper() {
        if (helper == null) {
            synchronized (AdmApi.class) {
                if (helper == null) {
                    helper = new AdmApi();
                }
            }
        }
        return helper;
    }

    public AdmApi() {
        okHttpClient = new OkHttpClient();
        try {
            okHttpClient = new okhttp3.OkHttpClient.Builder()
                    .sslSocketFactory(getSSLSocketFactory())
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();
        } catch (Exception e) {
            okHttpClient = new OkHttpClient();
        }

    }


    //客户端不对服务器证书做任何验证
    public SSLSocketFactory getSSLSocketFactory() throws Exception {
        //创建一个不验证证书链的证书信任管理器。
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts,
                new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        return sslContext.getSocketFactory();
    }


    private OkHttpClient okHttpClient;

    private static final String BASE_URL = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=10";


    private String getJsonParams(Map<String, String> map) {
        String jsonParam = "";
        JSONObject jsonObject = new JSONObject();
        try {
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            }
            jsonParam = jsonObject.toString();
        } catch (Exception e) {
        }
        return jsonParam;
    }


    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType
            .parse("text/x-markdown; charset=utf-8");

    private void d(String s) {
        L.d("apitag", s);
    }


    //eclipse获取versionCode和versionName 这两个参数是写在manifest.xml文件中
    public int getVersionCode(Context context) {
        int versioncode = -1;
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versioncode;
    }


    /**
     * 通过接口，获得每个广告key展示误点击广告的概率
     */
    public void getProbs(Context context) {

        d(" getProbs  start");
        ThreadPool.Companion.runThread(() -> {

            /**
             language	String	手机系统语言
             UUID	String	手机唯一标识
             VER	String	应用版本
             * */

            int versionCode = getVersionCode(context);

            String url = BASE_URL;
            Map<String, String> map = new HashMap<>();

            String req = getJsonParams(map);
            d(" getProbs  req = " + req);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, req))
                    .build();

            try {
                okhttp3.Response execute = okHttpClient.newCall(request).execute();
                String string = execute.body().string();

                d(" getProbs  string = " + string);
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    JSONArray images = jsonObject.getJSONArray("images");
                    List<ImageBean> list = new ArrayList<>();
                    for (int i = 0; i < images.length(); i++) {
                        JSONObject image = images.getJSONObject(i);
                        String enddate = image.getString("enddate");
                        String imageUrl = image.getString("url");
                        String copyright = image.getString("copyright");
                        ImageBean imageBean = new ImageBean(imageUrl, enddate, copyright);
                        list.add(imageBean);

                        DownloadUtil.get().download(context, ImageBean.BASE_URL + imageUrl,
                                new OnDownloadListener() {
                                    @Override
                                    public void onDownloadSuccess() {
                                        d("onDownloadSuccess");
                                    }

                                    @Override
                                    public void onDownloading(int progress) {
//                                        d("onDownloading");
                                    }

                                    @Override
                                    public void onDownloadFailed(String err) {
                                        d("onDownloadFailed  err="+err);
                                    }
                                });
                    }
                    EventBus.getDefault().post(new ImageEvent(list));

                    d(" getProbs  updated");

//
                } catch (Exception e) {
                    d(" getProbs  e1 = " + e.toString());

                }
            } catch (Exception e) {
                d(" getProbs  e2 = " + e.toString());

            }
        });
    }


}
