package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Car;
import com.lenovo.smarttraffic.ui.adapter.BasePagerAdapter;
import com.lenovo.smarttraffic.util.CommonUtil;

import java.util.List;

import butterknife.BindView;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class QueryCarActivity extends BaseActivity {

    private String violations;
    private EditText et_cp;
    private Button bt_cp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_car_query;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "违章查询");
        et_cp = findViewById(R.id.et_cp);
        bt_cp = findViewById(R.id.bt_cp);
    }

    private void InitData() {
        violations = InitApp.sp.getString("violations", "");
        List<Car.ROWSDETAILBean> carInfo = new Gson().fromJson(InitApp.sp.getString("carInfo", ""), Car.class).getROWS_DETAIL();

        bt_cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String carnumber = "鲁"+et_cp.getText().toString().trim().toUpperCase();
                for (int i = 0; i < carInfo.size() ; i++) {
                    if (carInfo.get(i).getCarnumber().equals(carnumber)) {
                        InitApp.edit.putString("vuserinfo", carInfo.get(i).getPcardid()).commit();
                        if (violations.indexOf(carnumber) != -1) {
                            startActivity(new Intent(getApplicationContext(), ViolationActivity.class));
                            return;
                        }
                    }
                }
                startActivity(new Intent(getApplicationContext(), WuActivity.class));
            }
        });
    }


    @Override
    protected void onResume() {
        et_cp.setText("");

        super.onResume();
    }

}
