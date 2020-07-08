package com.example.bing.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import com.afpackage.utils.kt.base.BaseRecyclerViewAdapter1.OnItemClickListener;
import com.example.bing.bases.MyBaseActivity;
import com.example.bing.beans.ImageBean;
import com.example.bing.ui.adapters.ImageAdapter;
import com.example.bing.utils.db.Image_DB;
import gasds.R;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class HistoryActivity extends MyBaseActivity {

    @Override
    public int setContentId() {
        return R.layout.activity_history;
    }

    @BindView(R.id.rv_ah_images)
    RecyclerView rv_ah_images;

    public static final int RESULT_CODE_IMAGE = 3001;

    private List<ImageBean> mData   = new ArrayList<>();
    private ImageAdapter    adapter = new ImageAdapter(this, mData);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rv_ah_images.setLayoutManager(new LinearLayoutManager(this));
        rv_ah_images.setAdapter(adapter);

        mData.addAll(Image_DB.getInstance().queryAll());
        adapter.notifyDataSetChanged();

        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, @NotNull View v) {

                Intent data = new Intent();
                data.putExtra("image", mData.get(position).dateFormat);
                setResult(RESULT_CODE_IMAGE, data);
                finish();
            }
        });
    }


}
