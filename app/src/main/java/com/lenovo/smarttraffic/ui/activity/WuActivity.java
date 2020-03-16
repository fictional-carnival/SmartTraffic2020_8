package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lenovo.smarttraffic.R;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class WuActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_wu;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "查询结果");
    }

    private void InitData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
