package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Ditu;
import com.lenovo.smarttraffic.ui.adapter.BasePagerAdapter;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class ParkInfoActivity extends BaseActivity {
    private Ditu.ROWSDETAILBean ditu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int i = getIntent().getIntExtra("i", 0);
        ditu = new Gson().fromJson(InitApp.sp.getString("ditu", ""), Ditu.class).getROWS_DETAIL().get(i);
        InitView();
        InitData();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_parkinfo;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "停车场详情");
        TextView tv_1 = findViewById(R.id.tv_1);
        TextView tv_2 = findViewById(R.id.tv_2);
        TextView tv_d = findViewById(R.id.tv_d);
        TextView tv_address = findViewById(R.id.tv_address);
        TextView tv_name = findViewById(R.id.tv_name);

        tv_name.setText(ditu.getName());
        tv_address.setText(ditu.getAddress());
        tv_d.setText(ditu.getDistance()+"米");
        tv_2.setText(ditu.getEmptySpace()+"个/"+ditu.getAllSpace()+"个");
        Log.i("asfsaf", "InitView: "+ditu.getEmptySpace()+"个/"+ditu.getAllSpace()+"个");
    }

    private void InitData() {
        BasePagerAdapter basePagerAdapter = new BasePagerAdapter(getSupportFragmentManager());

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


}
