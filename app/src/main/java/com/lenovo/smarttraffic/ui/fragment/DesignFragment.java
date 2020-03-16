package com.lenovo.smarttraffic.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.lenovo.smarttraffic.Constant;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author Amoly
 * @date 2019/4/11.
 * description：设计页面
 */
public class DesignFragment extends BaseFragment {
    private static DesignFragment instance = null;
    private LineChart lineChart;
    private PieChart pieChart;
    private TextView tv_2;
    private TextView tv_1;
    private Timer timer;
    private long time;
    private String[] load_status;
    private int[] colors;
    private int[] colors2;
    private String[] senses;
    private String[] loads;

    public static DesignFragment getInstance() {
        if (instance == null) {
            instance = new DesignFragment();
        }
        return instance;
    }


    @Override
    protected View getSuccessView() {
        View view = View.inflate(getContext(), R.layout.design_fragment, null);
        lineChart = view.findViewById(R.id.lineChart);
        pieChart = view.findViewById(R.id.pieChart);
        tv_1 = view.findViewById(R.id.tv_1);
        tv_2 = view.findViewById(R.id.tv_2);
        initData();

        return view;
    }

    private void initData() {
        senses = new String[]{"温度", "PM", "CO2", "光照"};
        load_status = new String[]{"很差", "差", "一般", "良好", "优"};
        colors = new int[]{
                Color.parseColor("#B93629"),
                Color.parseColor("#324654"),
                Color.parseColor("#8EB7B9"),
                Color.parseColor("#C6856A"),
                Color.parseColor("#92C5AF"),
                Color.parseColor("#769C84"),
                Color.parseColor("#C88625"),
        };
        colors2 = new int[]{
                Color.parseColor("#B93629"),
                Color.parseColor("#324654"),
                Color.parseColor("#8EB7B9"),
                Color.parseColor("#C6856A"),
        };
        loads = new String[]{"学院路", "联通路", "医院路", "幸福路", "环城高速", "环城快速路", "停车场"};
        time = new Date().getTime();
        initLine();
        initPie();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time = new Date().getTime();
                        lineChart.clear();
                        pieChart.clear();
                        initLine();
                        initPie();
                    }
                });
            }
        },5000,5000);
        initLine();
        initPie();
    }

    private void initPie() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        for (int i = 0; i < loads.length; i++) {
            pieEntries.add(new PieEntry(InitApp.random(1, 5), loads[i]));
        }
        colors = new int[]{
                Color.parseColor("#B93629"),
                Color.parseColor("#324654"),
                Color.parseColor("#8EB7B9"),
                Color.parseColor("#C6856A"),
                Color.parseColor("#92C5AF"),
                Color.parseColor("#769C84"),
                Color.parseColor("#C88625"),
        };
        pieChart.setExtraOffsets(10,10,10,10);
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
        //图例设置为无
//        legend.setEnabled(false);
        pieDataSet.setValueTextSize(0);
        pieChart.setEntryLabelColor(Color.parseColor("#326AA8"));
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//lable外部展示
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//value外部展示
        pieDataSet.setValueLinePart1Length(0.3f);//
        pieChart.setCenterTextColor(Color.GREEN);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleColor(Color.parseColor("#96BDE4"));
        pieChart.setTouchEnabled(false);
        pieChart.setCenterTextColor(Color.parseColor("#009688"));
        pieChart.setCenterText("实时道路拥堵状况");
        pieChart.setCenterTextSize(20);
        pieDataSet.setValueLinePart2Length(0.3f);//组成折线的两段折线长短
        //折线的颜色
        pieDataSet.setValueLineColor(Color.parseColor("#cccccc"));
        //折线的宽度
        pieDataSet.setValueLineWidth(3);
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
    }

    private void initLine() {
        initX();
        initY();
        initLegend();
        initDataSet();

    }

    private void initDataSet() {
        lineChart.setExtraOffsets(50,10,50,20);
        ArrayList<ArrayList<Entry>> arrayLists = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ArrayList<Entry> arrayList = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                arrayList.add(new Entry(j, InitApp.random(1, 5)));
            }
            arrayLists.add(arrayList);
        }

        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            LineDataSet lineDataSet = new LineDataSet(arrayLists.get(i), senses[i]);
            lineDataSet.setValueTextSize(0);
            lineDataSet.setColor(colors2[i]);
            lineDataSets.add(lineDataSet);
        }
        LineData lineData = new LineData(lineDataSets.get(0),
                lineDataSets.get(1),
                lineDataSets.get(2),
                lineDataSets.get(3)
        );
        lineChart.setData(lineData);
    }

    private void initLegend() {
        Legend legend = lineChart.getLegend();
        lineChart.getDescription().setEnabled(false);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(20);
        legend.setFormLineWidth(15);
        legend.setFormSize(10);
        legend.setYOffset(20);
    }

    private void initY() {
        lineChart.getAxisRight().setEnabled(false);
        YAxis left = lineChart.getAxisLeft();
        left.setLabelCount(5);
        left.setTextSize(20);
        left.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return load_status[(int)--value];
            }
        });
    }

    private void initX() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(20);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return InitApp.timeFormat(new Date((time-(6-(int)Math.ceil(value))*3000)),"mm:ss");
            }
        });
        xAxis.setDrawGridLines(false);
    }

    @Override
    public void onDestroyView() {
        timer.cancel();
        super.onDestroyView();
    }

    @Override
    protected Object requestData() {
        return Constant.STATE_SUCCEED;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDestroy() {
        if (instance != null) {
            instance = null;
        }
        super.onDestroy();
    }

}
