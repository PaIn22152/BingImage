package com.example.bing.ui.activities;

import android.Manifest.permission;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnLongClick;
import com.example.bing.beans.ImageBean;
import com.example.bing.utils.ApiHelper;
import com.example.bing.background.events.ImageEvent;
import com.example.bing.bases.MyBaseActivity;

import com.example.bing.utils.DownloadUtil;
import com.example.bing.utils.DownloadUtil.OnDownloadListener;
import com.example.bing.utils.ImageLoader;
import com.example.bing.utils.L;
import com.example.bing.utils.db.Image_DB;
import gasds.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends MyBaseActivity {

    @Override
    public int setContentId() {
        return R.layout.activity_main;
    }


    @BindView(R.id.iv_am_image)
    ImageView iv_am_image;
    @BindView(R.id.tv_am_des)
    TextView  tv_am_des;


    @OnLongClick({R.id.rl_am_root})
    public void longClick(View view) {
        switch (view.getId()) {
            case R.id.rl_am_root:
                showDialog2Download();
                break;
        }
    }

    private void showDialog2Download() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_download, null);

        v.findViewById(R.id.tv_aa_save).setOnClickListener(vv -> {
            if (imageBean != null) {
                DownloadUtil.get().download(this, imageBean, new OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        L.d("onDownloadSuccess");
                    }

                    @Override
                    public void onDownloading(int progress) {
                        L.d("onDownloading");
                    }

                    @Override
                    public void onDownloadFailed(String err) {
                        L.d("onDownloadFailed");
                    }
                });
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

        updateUI();

        requestPermission4NormalManifest(permission.WRITE_EXTERNAL_STORAGE,
                new OnPermissionRequestCallback() {
                    @Override
                    public void onGrant() {

                    }

                    @Override
                    public void onDeny() {

                    }

                    @Override
                    public void onDeniedAndNotAsk() {

                    }
                });
    }


    ImageBean imageBean;

    private void updateUI() {
        imageBean = Image_DB.getInstance().queryToday();
        if (imageBean != null) {
            ImageLoader.load(this, iv_am_image, imageBean);
            tv_am_des.setText(imageBean.copyright);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void image(ImageEvent event) {

        updateUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
