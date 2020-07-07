package com.example.bing;

import android.Manifest.permission;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends MyBaseActivity {

    @Override
    public int setContentId() {
        return R.layout.activity_main;
    }

//    @BindView(R.id.rv_am_images)
//    RecyclerView rv_am_images;

    List<ImageBean> mData = new ArrayList<>();
    private ImageAdapter adapter = new ImageAdapter(this, mData);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        AdmApi.getHelper().getProbs(this);

        RecyclerView rv_am_images=findViewById(R.id.rv_am_images);
        rv_am_images.setLayoutManager(new LinearLayoutManager(this));
        rv_am_images.setAdapter(adapter);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void image(ImageEvent event) {
        mData.addAll(event.list);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
