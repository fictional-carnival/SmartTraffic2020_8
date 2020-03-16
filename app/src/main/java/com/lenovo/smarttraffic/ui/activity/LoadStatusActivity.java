package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.ui.adapter.BasePagerAdapter;
import com.lenovo.smarttraffic.util.CommonUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import lecho.lib.hellocharts.formatter.ValueFormatterHelper;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class LoadStatusActivity extends BaseActivity {

    private BarChart barChart;
    private String[] loads;
    private String[] load_status;
    private int[] colors;
    private String[] week;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_load_status;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "路况分析");
        barChart = findViewById(R.id.barChart);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void InitData() {
        loads = new String[]{"学院路", "联通路", "医院路", "幸福路", "环城高速", "环城快速路", "停车场"};
        load_status = new String[]{"通畅", "缓行", "轻度拥堵", "一般拥堵", "重度拥堵"};
        colors = new int[]{
                Color.parseColor("#BC3A2C"),
                Color.parseColor("#2E4451"),
                Color.parseColor("#659FA9"),
                Color.parseColor("#CD8264"),
                Color.parseColor("#91C6AF"),
                Color.parseColor("#759C83"),
                Color.parseColor("#C98523"),
        };
        week = new String[]{"周一","周二","周三","周四","周五","周六","周日",};
        initChart();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        barChart.clear();
                        initChart();
                    }
                });
            }
        },5000,5000);
    }

    private void initChart() {
        initX();
        initY();
        initLegend();

        initDataSet();
}

    private void initDataSet() {
        ArrayList<ArrayList<BarEntry>> bs = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                barEntries.add(new BarEntry(j, InitApp.random(1, 5)));
            }

            bs.add(barEntries);
        }


        ArrayList<BarDataSet> barDataSets = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            BarDataSet barDataSet = new BarDataSet(bs.get(i), loads[i]);

            barDataSet.setColor(colors[i]);
            barDataSet.setValueTextSize(0);

            barDataSets.add(barDataSet);
        }

        BarData barData = new BarData(
                barDataSets.get(0),
                barDataSets.get(1),
                barDataSets.get(2),
                barDataSets.get(3),
                barDataSets.get(4),
                barDataSets.get(5),
                barDataSets.get(6)

        );

        int a = 7;
        float b = 0.3f;
        float c = (1f-b)/a;
        float d = 0f;
        barData.setBarWidth(c);
        barData.groupBars(-0.5f, b, d);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setExtraOffsets(30,30,30,30);
        
    }


    private void initLegend() {
        Legend legend = barChart.getLegend();
        legend.setYOffset(20);
        legend.setFormSize(30);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setTextSize(15);
        legend.setFormLineWidth(10);
    }

    private void initY() {
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setTextSize(20);
        axisRight.setLabelCount(5);
        axisRight.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)value+"";
            }

        });
        YAxis left = barChart.getAxisLeft();
        left.setTextSize(20);
        left.setLabelCount(4);
        left.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return load_status[(int) --value];
            }

        });
    }

    private void initX() {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(20);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return week[(int) Math.ceil(value)];
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
