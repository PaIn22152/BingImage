package com.example.bing.bases;

import android.os.Bundle;
import butterknife.ButterKnife;
import com.afpackage.utils.kt.base.BaseActivity;
import com.afpackage.utils.kt.base.RequestPermissionActivity;
import com.example.bing.utils.L;
import org.jetbrains.annotations.Nullable;

/**
 * Project    Bing
 * Path       com.example.bing
 * Date       2020/07/07 - 14:46
 * Author     Payne.
 * About      类描述：
 */
public abstract class MyBaseActivity extends RequestPermissionActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
    }
}
