package com.example.bing.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.afpackage.utils.kt.base.BaseRecyclerViewAdapter1;
import com.afpackage.utils.kt.base.BaseViewHolder;
import com.example.bing.beans.ImageBean;
import gasds.R;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Project    Bing
 * Path       com.example.bing
 * Date       2020/07/07 - 15:03
 * Author     Payne.
 * About      类描述：
 */
public class ImageAdapter extends BaseRecyclerViewAdapter1 {

    private Context         mContext;
    private List<ImageBean> mData;

    public ImageAdapter(Context mContext, List<ImageBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image, null);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull BaseViewHolder viewholer, int position) {
        super.onBindViewHolder(viewholer, position);
        viewholer.setImageViewPath(R.id.iv_ii_image, mData.get(position).getFullUrl());
        viewholer.setTextViewText(R.id.tv_ii_title, mData.get(position).dateFormat);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
