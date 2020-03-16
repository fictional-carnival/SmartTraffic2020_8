package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class ViolaionPayActivity extends BaseActivity {

    private TextView tv_money;
    private TextView tv_cancel;
    private TextView tv_com;
    private TextView tv_title;
    private int io;
    private String username;
    private JSONArray jsonArray;
    private ImageView im_1;
    private LinearLayout linearLayout;
    private Timer timer;
    private long index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("u");
        io = getIntent().getIntExtra("p", 0);
        try {
            jsonArray = new JSONArray(InitApp.sp.getString(username, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InitView();
        InitData();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_violation_pay;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "二维码支付");
        tv_money = findViewById(R.id.tv_money);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_com = findViewById(R.id.tv_com);
        tv_title = findViewById(R.id.tv_dder);
        im_1 = findViewById(R.id.tv_er);
        linearLayout = findViewById(R.id.ll_ll);
    }

    private void InitData() {
        try {
            tv_money.setText(jsonArray.getJSONObject(io).getString("pmoney"));
            tv_title.setText(jsonArray.getJSONObject(io).getString("carnumber")+",付款金额="+jsonArray.getJSONObject(io).getString("pmoney")+"元");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        im_1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
                tv_com.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            jsonArray.getJSONObject(io).put("pchuli", 1);
                            InitApp.edit.putString(username, jsonArray.toString()).commit();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linearLayout.setVisibility(View.GONE);
                    }
                });
                return false;
            }
        });
            timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        index++;
                        if (index % 2 == 0) {
                            im_1.setImageResource(R.mipmap.icon_erweima);
                        } else {
                            im_1.setImageResource(R.drawable.erweima2);

                        }
                    }
                });
            }
        },5000,5000);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
