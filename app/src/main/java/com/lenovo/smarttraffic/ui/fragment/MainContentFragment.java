package com.lenovo.smarttraffic.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.lenovo.smarttraffic.Constant;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Weather;
import com.lenovo.smarttraffic.ui.activity.CheWeiActivity;
import com.lenovo.smarttraffic.ui.activity.Activity_QianDao;
import com.lenovo.smarttraffic.ui.activity.Activity_Weather;
import com.lenovo.smarttraffic.ui.activity.DitieActivity;
import com.lenovo.smarttraffic.ui.activity.Item1Activity;
import com.lenovo.smarttraffic.ui.activity.LoginActivity;
import com.lenovo.smarttraffic.ui.activity.QueryCarActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Amoly
 * @date 2019/4/11.
 * description：主页面
 */
public class MainContentFragment extends BaseFragment {
    private static MainContentFragment instance = null;
    public View view;
    public PieChart pieChart;
    public TextView test_day;
    public TextView tv_day;
    public TextView test_year;
    public TextView tv_year;
    public ImageView iv_year;
    public ImageView iv_day;
    public TextView tv_1;
    public TextView tv_2;
    public TextView tv_3;
    public TextView tv_4;
    public Timer mTimer;

    public static MainContentFragment getInstance() {
        if (instance == null) {
            instance = new MainContentFragment();
        }
        return instance;
    }

    @Override
    protected View getSuccessView() {
        view = View.inflate(getActivity(), R.layout.fragment_home, null);
        initUI();
        getWeather();
        return view;
    }

    private void getWeather() {
        InitApp.doPost("GetWeather", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                InitApp.edit.putString("GetWeather", String.valueOf(jsonObject)).commit();
                Weather weather = new Gson().fromJson(String.valueOf(jsonObject), Weather.class);
                test_day.setText("今日天气      " + weather.getWCurrent() + "°");
                List<Weather.ROWSDETAILBean> beanList = weather.getROWS_DETAIL();
                String dtype = beanList.get(1).getType();
                tv_day.setText(dtype);
                iv_day.setImageResource(isType(dtype));
                test_year.setText("明日天气     " + beanList.get(2).getTemperature() + "°");
                String ytype = beanList.get(2).getType();
                tv_year.setText(ytype);
                iv_year.setImageResource(isType(ytype));
            }
        });
    }

    private int isType(String type) {
        switch (type) {
            case "晴":
                return R.mipmap.sun;
            case "阴":
                return R.mipmap.cloudy_;
            case "小雨":
                return R.mipmap.rain;
        }
        return R.mipmap.cloudy;
    }

    @Override
    public void onStop() {
        super.onStop();
        mTimer.cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setIn();
                        pieChart.clear();
                        setPieChart();
                    }
                });
            }
        },0,3000);
    }

    private void setPieChart() {
        int pie = InitApp.random(8, 100);
        ArrayList<Integer> colors = new ArrayList<Integer>() {{
            add(Color.parseColor("#993300"));
            add(Color.parseColor("#33CC00"));
        }};
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>() {{
            add(new PieEntry(pie, ""));
            add(new PieEntry(100 - pie, ""));
        }};
        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(0);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setHoleRadius(75);
        pieChart.setTouchEnabled(false);
        pieChart.setEnabled(false);
        pieChart.setCenterText("优");
        pieChart.getDescription().setText("");
        pieChart.setCenterTextSize(20);
        if (pie >0 && pie <20){
            pieChart.setCenterText("优");
        }else  if (pie >=20 && pie <=40){
            pieChart.setCenterText("良");
        }else  if (pie >40 && pie <60){
            pieChart.setCenterText("轻度污染");
        }else  if (pie >=60 && pie <=80){
            pieChart.setCenterText("中度污染");
        }else {
            pieChart.setCenterText("重度污染");
        }
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        pieChart.setClickable(false);
    }

    private void setIn() {
        int ran1 = InitApp.random(80, 5000);
        int ran2 = InitApp.random(80, 7000);
        int ran3 = InitApp.random(8, 50);
        int ran4 = InitApp.random(8, 100);

        if (ran1 > 0 && ran1<1000){
            tv_1.setText("弱");
            tv_1.setTextColor(Color.parseColor("#4472c4"));
        }else if (ran1 >= 1000 && ran1<=3000){
            tv_1.setText("中等");
            tv_1.setTextColor(Color.parseColor("#00b050"));
        }else {
            tv_1.setText("强");
            tv_1.setTextColor(Color.parseColor("#ff0000"));
        }

        if (ran2 > 0 && ran2<3000){
            tv_2.setText("适宜");
            tv_2.setTextColor(Color.parseColor("#44dc68"));
        }else if (ran2 >= 3000 && ran2<=6000){
            tv_2.setText("中");
            tv_2.setTextColor(Color.parseColor("#ffc000"));
        }else {
            tv_2.setText("较不宜");
            tv_2.setTextColor(Color.parseColor("#8149ac"));
        }

        if (ran3 > 0 && ran3<12){
            tv_3.setText("冷");
            tv_3.setTextColor(Color.parseColor("#3462f4"));
        }else if (ran3 >= 12 && ran3<=21){
            tv_3.setText("舒适");
            tv_3.setTextColor(Color.parseColor("#92d050"));
        } else if (ran3 >= 21 && ran3<=35){
            tv_3.setText("温暖");
            tv_3.setTextColor(Color.parseColor("#44dc68"));
        }else {
            tv_3.setText("热");
            tv_3.setTextColor(Color.parseColor("#ff0000"));
        }

        if (ran4 > 0 && ran4<50){
            tv_4.setText("较易发");
            tv_4.setTextColor(Color.parseColor("#ff0000"));
        }else {
            tv_4.setText("少发");
            tv_4.setTextColor(Color.parseColor("#ffff40"));
        }

    }

    private void initUI() {
        pieChart = view.findViewById(R.id.pieChart);
        test_day = view.findViewById(R.id.test_day);
        tv_day = view.findViewById(R.id.tv_day);
        test_year = view.findViewById(R.id.test_year);
        tv_year = view.findViewById(R.id.tv_year);
        iv_year = view.findViewById(R.id.iv_year);
        iv_day = view.findViewById(R.id.iv_day);
        tv_1 = view.findViewById(R.id.tv_1);
        tv_2 = view.findViewById(R.id.tv_2);
        tv_3 = view.findViewById(R.id.tv_3);
        tv_4 = view.findViewById(R.id.tv_4);
        LinearLayout ll_tq = view.findViewById(R.id.ll_tq);
        ll_tq.setOnClickListener(this::onClick);
        RelativeLayout rl_1 = view.findViewById(R.id.rl_1);
        rl_1.setOnClickListener(this::onClick);
        RelativeLayout rl_2 = view.findViewById(R.id.rl_2);
        rl_2.setOnClickListener(this::onClick);
        RelativeLayout rl_3 = view.findViewById(R.id.rl_3);
        rl_3.setOnClickListener(this::onClick);
        RelativeLayout rl_4 = view.findViewById(R.id.rl_4);
        rl_4.setOnClickListener(this::onClick);

    }


    @Override
    protected Object requestData() {
        return Constant.STATE_SUCCEED;
    }

    @Override
    public void onClick(View view) {
        if (InitApp.sp.getBoolean("isLogin", false)) {
            switch (view.getId()) {
                case R.id.ll_tq:
                    startActivity(new Intent(getContext(), Activity_Weather.class));
                    break;
                case R.id.rl_1:
                    startActivity(new Intent(getContext(), QueryCarActivity.class));
                    break;
                case R.id.rl_2:
                    startActivity(new Intent(getContext(), CheWeiActivity.class));
                    break;
                case R.id.rl_3:
                    startActivity(new Intent(getContext(), DitieActivity.class));
                    break;
                case R.id.rl_4:
                    startActivity(new Intent(getContext(), Activity_QianDao.class));

                    break;
            }
        }else {
            InitApp.toast("您未登录，请登录后查看");
        }
    }

    @Override
    public void onDestroy() {
        if (instance != null) {
            instance = null;
        }
        super.onDestroy();
    }


}
