package com.example.bing.ui.activities;

import static com.example.bing.ui.activities.HistoryActivity.RESULT_CODE_IMAGE;

import android.Manifest.permission;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
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
import gasds.R;
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
    ImageView iv_am_image;
    @BindView(R.id.tv_am_des)
    TextView  tv_am_des;

    @BindView(R.id.rl_am_menu)
    RelativeLayout rl_am_menu;
    @BindView(R.id.ll_am_menu)
    LinearLayout   ll_am_menu;


    @OnClick({R.id.rl_am_root, R.id.tv_am_history,
            R.id.rl_am_menu, R.id.tv_am_download})
    public void longClick(View view) {
        switch (view.getId()) {
            case R.id.rl_am_root:
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
                                        ToastUtil.showLong("本地已存在");
                                    }

                                    @Override
                                    public void onDownloadSuccess(String path) {
                                        L.d("onDownloadSuccess");
                                        ToastUtil.showLong("下载成功");
                                        String[] paths = {
                                                path
                                        };
                                        MediaScannerConnection
                                                .scanFile(MainActivity.this, paths, null, null);

                                    }

                                    @Override
                                    public void onDownloading(int progress) {
                                        L.d("onDownloading");
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
