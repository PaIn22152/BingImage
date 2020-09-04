package com.example.bing.ui.activities;

import static com.example.bing.ui.activities.HistoryActivity.RESULT_CODE_IMAGE;

import android.Manifest.permission;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afpackage.utils.kt.ScreenAdaptUtilKt;
import com.bumptech.glide.Glide;
import com.example.bing.beans.ImageBean;
import com.example.bing.utils.ApiHelper;
import com.example.bing.background.events.ImageEvent;
import com.example.bing.bases.MyBaseActivity;

import com.example.bing.utils.Convert;
import com.example.bing.utils.DownloadUtil;
import com.example.bing.utils.DownloadUtil.OnDownloadListener;
import com.example.bing.utils.ImageLoader;
import com.example.bing.utils.L;
import com.example.bing.utils.ToastUtil;
import com.example.bing.utils.db.Image_DB;
import com.github.chrisbanes.photoview.PhotoView;
import gasds.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

public class MainActivity extends MyBaseActivity {

    @Override
    public int setContentId() {
        return R.layout.activity_main;
    }


    private static final int REQUEST_CODE_HISTORY = 1001;
    @BindView(R.id.iv_am_image)
    PhotoView iv_am_image;
    @BindView(R.id.tv_am_des)
    TextView  tv_am_des;

    @BindView(R.id.rl_am_menu)
    RelativeLayout rl_am_menu;
    @BindView(R.id.ll_am_menu)
    LinearLayout   ll_am_menu;

    @Override
    public void beforeSetContent() {
        super.beforeSetContent();
        ScreenAdaptUtilKt.adaptScreen(this);
    }

    @OnClick({R.id.rl_am_root, R.id.tv_am_history, R.id.tv_am_copy,
            R.id.rl_am_menu, R.id.tv_am_download, R.id.rl_am_more,
            R.id.tv_am_wallpaper
    })
    public void longClick(View view) {
        switch (view.getId()) {
            case R.id.tv_am_wallpaper:
                hideMenu();
                showDialog2SetWallpaper();
                break;
            case R.id.rl_am_more:
                showMenu();
                break;
            case R.id.rl_am_menu:
                hideMenu();
                break;
            case R.id.tv_am_history:
                startActivityForResult(new Intent(this, HistoryActivity.class),
                        REQUEST_CODE_HISTORY);
                hideMenu();
                break;
            case R.id.tv_am_download:
                hideMenu();
                downloadImage();
                break;
            case R.id.tv_am_copy:
                hideMenu();
                copyUrl();
                break;
        }
    }

    private void showMenu() {
        rl_am_menu.setVisibility(View.VISIBLE);

        ll_am_menu.setTranslationY(ll_am_menu.getMeasuredHeight());
        ll_am_menu.animate().translationY(0).setDuration(300).setListener(null).start();


    }

    private void hideMenu() {
        ll_am_menu.animate().translationY(ll_am_menu.getMeasuredHeight()).setDuration(300)
                .setListener(
                        new AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ll_am_menu.setTranslationY(ll_am_menu.getMeasuredHeight());
                                rl_am_menu.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).start();
    }

    private void downloadImage() {
        downloadImage(false, 0);
    }

    private void downloadImage(boolean setWallpaper, int type) {
        requestPermission4NormalManifest(permission.WRITE_EXTERNAL_STORAGE,
                new OnPermissionRequestCallback() {
                    @Override
                    public void onGrant() {
                        DownloadUtil.get()
                                .download(MainActivity.this, imageBean, new OnDownloadListener() {
                                    /**
                                     * 已经下载到本地了
                                     *
                                     * @param localPath
                                     */
                                    @Override
                                    public void onDownloaded(String localPath) {
                                        if (setWallpaper) {
                                            setWallpaper(localPath, type);
                                        } else {
                                            ToastUtil.showLong("本地已存在");
                                        }
                                    }

                                    @Override
                                    public void onDownloadSuccess(String path) {
                                        L.d("onDownloadSuccess");

                                        if (setWallpaper) {
                                            setWallpaper(path, type);
                                        } else {
                                            ToastUtil.showLong("下载成功");
                                        }

                                        updateSys(new File(path));


                                    }

                                    @Override
                                    public void onDownloading(int progress) {
                                        L.d("onDownloading  progress=" + progress);
                                    }

                                    @Override
                                    public void onDownloadFailed(String err) {
                                        L.d("onDownloadFailed  err=" + err);
                                    }
                                });
                    }

                    @Override
                    public void onDeny() {

                    }

                    @Override
                    public void onDeniedAndNotAsk() {

                    }
                });
    }

    private static final int WALLPAPER_TYPE_SYSTEM = 303;
    private static final int WALLPAPER_TYPE_LOCK   = 304;
    private static final int WALLPAPER_TYPE_ALL    = 305;

    private void showDialog2SetWallpaper() {
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_set_wallpaper, null);
            LinearLayout ll_dsw_sys = view.findViewById(R.id.ll_dsw_sys);
            LinearLayout ll_dsw_lock = view.findViewById(R.id.ll_dsw_lock);
            LinearLayout ll_dsw_all = view.findViewById(R.id.ll_dsw_all);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            ll_dsw_sys.setOnClickListener(v -> {
                dialog.dismiss();
                downloadImage(true, WALLPAPER_TYPE_SYSTEM);
            });
            ll_dsw_lock.setOnClickListener(v -> {
                dialog.dismiss();
                downloadImage(true, WALLPAPER_TYPE_LOCK);
            });
            ll_dsw_all.setOnClickListener(v -> {
                dialog.dismiss();
                downloadImage(true, WALLPAPER_TYPE_ALL);
            });
            dialog.show();
        } else {
            downloadImage(true, WALLPAPER_TYPE_ALL);
        }
    }


    private void setWallpaper(String path, int type) {
        try {
            WallpaperManager manager = WallpaperManager.getInstance(this);
            if (VERSION.SDK_INT >= VERSION_CODES.N) {

                if (type == WALLPAPER_TYPE_SYSTEM) {
                    //桌面壁纸
                    manager.setStream(new FileInputStream(path), null, true,
                            WallpaperManager.FLAG_SYSTEM);
                } else if (type == WALLPAPER_TYPE_LOCK) {
                    //锁屏壁纸
                    manager.setStream(new FileInputStream(path), null, true,
                            WallpaperManager.FLAG_LOCK);
                } else {
                    //桌面壁纸
                    manager.setStream(new FileInputStream(path), null, true,
                            WallpaperManager.FLAG_SYSTEM);
                    //锁屏壁纸
                    manager.setStream(new FileInputStream(path), null, true,
                            WallpaperManager.FLAG_LOCK);
                }
                ToastUtil.showLong("设置壁纸成功");
            } else {
                manager.setStream(new FileInputStream(path));
                ToastUtil.showLong("设置壁纸成功");
            }
        } catch (Exception e) {
            L.d("");
        }
    }


    private void updateSys(File file) {
        //把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 通知图库更新
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Intent mediaScanIntent = new Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            mediaScanIntent.setData(uri);
                            sendBroadcast(mediaScanIntent);
                        }
                    });
        } else {
            String relationDir = file.getParent();
            File file1 = new File(relationDir);
            sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file1.getAbsoluteFile())));
        }
    }


    private void copyUrl() {
        if (imageBean != null) {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", imageBean.getFullUrl());
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            ToastUtil.showLong("已复制到剪贴板");
        }

    }


    private void showDialog2Download() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_download, null);

        v.findViewById(R.id.tv_aa_save).setOnClickListener(vv -> {
            if (imageBean != null) {

            }

        });

        builder.setView(v);
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        ApiHelper.getHelper().getImages(this);

        imageBean = Image_DB.getInstance().queryByDay(Convert.todayDateFormat());
        if (imageBean == null) {
            List<ImageBean> imageBeans = Image_DB.getInstance().queryAll();
            if (imageBeans.size() > 0) {
                imageBean = imageBeans.get(imageBeans.size() - 1);
            }
        }
        updateUI();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.d("");
        if (requestCode == REQUEST_CODE_HISTORY
                && resultCode == RESULT_CODE_IMAGE
                && data != null) {
            String dateFormat = data.getStringExtra("image");
            imageBean = Image_DB.getInstance().queryByDay(dateFormat);
            updateUI();
        }
    }

    ImageBean imageBean;

    private void updateUI() {

        if (imageBean != null) {
            ImageLoader.load(this, iv_am_image, imageBean);
            tv_am_des.setText(imageBean.copyright);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void image(ImageEvent event) {
        imageBean = Image_DB.getInstance().queryByDay(Convert.todayDateFormat());
        updateUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
