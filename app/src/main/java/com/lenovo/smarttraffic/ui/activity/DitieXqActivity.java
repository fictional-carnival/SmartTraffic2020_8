package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Line;

import java.util.List;

public class DitieXqActivity extends BaseActivity {

    public int load;
    public String site;
    public List<Line.ROWSDETAILBean> rowsDetail;
    public TextView tv_eeTime;
    public TextView tv_esTime;
    public TextView tv_end;
    public TextView tv_seTime;
    public TextView tv_ssTime;
    public TextView tv_start;
    public GridView gv_list;
    public TextView tv_load;
    public Line.ROWSDETAILBean bean;
    public MyAdapter myAdapter;
    public int status = -1;
    public PopupWindow popupWindow;

    @Override
    protected int getLayout() {
        return R.layout.activity_ditiexq;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar), true, "站点查询");
        load = getIntent().getIntExtra("load", 0);
        site = getIntent().getStringExtra("site");
        rowsDetail = new Gson().fromJson(getIntent().getStringExtra("row"), Line.class).getROWS_DETAIL();
        initUI();
        initData();
    }

    private void initData() {
        bean = rowsDetail.get(load);
        tv_load.setText(bean.getName());
        List<Line.ROWSDETAILBean.TimeBean> time = bean.getTime();
        Line.ROWSDETAILBean.TimeBean timeBean = time.get(0);
        tv_start.setText(timeBean.getSite());
        tv_ssTime.setText(timeBean.getStarttime());
        tv_seTime.setText(timeBean.getEndtime());
        Line.ROWSDETAILBean.TimeBean timeBean1 = time.get(1);
        tv_end.setText(timeBean1.getSite());
        tv_esTime.setText(timeBean1.getStarttime());
        tv_eeTime.setText(timeBean1.getEndtime());
        int size = bean.getSites().size();
        gv_list.setNumColumns(size);
        ViewGroup.LayoutParams layoutParams = gv_list.getLayoutParams();
        if (size < 13) {
            layoutParams.width = (1280 / size) * size;
        } else {
            layoutParams.width = 100 * size;
        }
        myAdapter = new MyAdapter();
        gv_list.setAdapter(myAdapter);
    }

    private void initUI() {
        TextView tv_dtxq_site = findViewById(R.id.tv_dtxq_site);
        tv_dtxq_site.setText(site);
        tv_load = findViewById(R.id.tv_load);
        TextView tv_money = findViewById(R.id.tv_money);
        tv_money.setText("全程票价："+String.valueOf(InitApp.random(2,12))+"元");
        tv_start = findViewById(R.id.tv_start);
        tv_ssTime = findViewById(R.id.tv_SsTime);
        tv_seTime = findViewById(R.id.tv_SeTime);
        tv_end = findViewById(R.id.tv_end);
        tv_esTime = findViewById(R.id.tv_EsTime);
        tv_eeTime = findViewById(R.id.tv_EeTime);
        gv_list = findViewById(R.id.gv_list);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return bean.getSites().size();
        }

        @Override
        public String getItem(int i) {
            return bean.getSites().get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (null == view) {
                view = View.inflate(viewGroup.getContext(), R.layout.item_ditiexq, null);
                viewHolder = new ViewHolder();
                viewHolder.bt_site = view.findViewById(R.id.bt_site);
                viewHolder.tv_site = view.findViewById(R.id.tv_site);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_site.setText(getItem(i));
            if (status == i) {
                viewHolder.bt_site.setBackgroundResource(R.drawable.button_checked);
            }else {
                viewHolder.bt_site.setBackgroundResource(R.drawable.button_unchecked);
            }
            viewHolder.bt_site.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    status = i;
                    if (bean.getTransfersites().indexOf(getItem(i)) != -1) {
                        showPopWindow(view, getItem(i));
                    }
                    //刷新
                    myAdapter.notifyDataSetChanged();
                }
            });
            return view;
        }
        public class ViewHolder {
            Button bt_site;
            TextView tv_site;
        }
    }

    private void showPopWindow(View view, String item) {
        if (null != popupWindow) {
            popupWindow.dismiss();
        }
        View inflate = getLayoutInflater().inflate(R.layout.item_pop_dtxq, null);
        popupWindow = new PopupWindow(inflate, -2, -2, false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); //这句代码必须要才能获得正确的popupwindow的宽度
        int popupwindowWidth = popupWindow.getContentView().getMeasuredWidth();
        int xoff = view.getWidth() / 2 - popupwindowWidth / 2 + 5;
        popupWindow.showAsDropDown(view, xoff, 50);
        TextView tv_qp = inflate.findViewById(R.id.tv_qp);
        tv_qp.setText(isHc(item));
    }

    private String isHc(String item) {
        for (int i = 0, l = rowsDetail.size(); i < l; i++) {
            if (i != load) {
                Line.ROWSDETAILBean beanHc = rowsDetail.get(i);
                if (beanHc.getTransfersites().indexOf(item) != -1) {
                    return "可换乘" + beanHc.getName();
                }
            }
        }
        return null;
    }
}
