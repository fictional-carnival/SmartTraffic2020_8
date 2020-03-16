package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Line;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class DitieActivity extends BaseActivity {

    public ExpandableListView el_dt;
    public Intent intent;
    public List<Line.ROWSDETAILBean> beanList;
    public PopupWindow popupWindow;

    @Override
    protected int getLayout() {
        return R.layout.activity_ditie;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar), true, "实时交通");
        el_dt = findViewById(R.id.el_dt);
        intent = new Intent(getApplicationContext(), DitieXqActivity.class);
        initData();
    }
    private void initData() {
        HashMap map = new HashMap();
        map.put("UserName", "User1");
        map.put("Line", 0);
        InitApp.doPost("GetMetroInfo", map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                intent.putExtra("row", String.valueOf(jsonObject));
                beanList = new Gson().fromJson(String.valueOf(jsonObject), Line.class).getROWS_DETAIL();
                el_dt.setAdapter(new MyAdapter());
            }
        });
    }

    private class MyAdapter extends BaseExpandableListAdapter {
        @Override
        public int getGroupCount() {
            return beanList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return beanList.get(i).getSites().size();
        }

        @Override
        public String getGroup(int i) {
            return beanList.get(i).getName();
        }

        @Override
        public String getChild(int i, int i1) {
            return beanList.get(i).getSites().get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            GroupViewHolder groupViewHolder;
            if (null == view) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dt_group, viewGroup, false);
                groupViewHolder = new GroupViewHolder();
                groupViewHolder.tv_group = view.findViewById(R.id.tv_group);
                view.setTag(groupViewHolder);
            }else{
                groupViewHolder = (GroupViewHolder) view.getTag();
            }
            groupViewHolder.tv_group.setText(getGroup(i));
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            ChildViewHolder childViewHolder;
            if (null == view) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dt_child, viewGroup, false);
                childViewHolder = new ChildViewHolder();
                childViewHolder.tv_child = view.findViewById(R.id.tv_child);
                view.setTag(childViewHolder);
            }else {
                childViewHolder = (ChildViewHolder) view.getTag();
            }
            childViewHolder.tv_child.setText(getChild(i, i1));
            childViewHolder.tv_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopWindow(view, getChild(i, i1), i);
                }
            });
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

        private class GroupViewHolder {
            TextView tv_group;
        }
        private class ChildViewHolder {
            TextView tv_child;
        }
    }

    private void showPopWindow(View view, String child, int i) {
        if (null != popupWindow) {
            popupWindow.dismiss();
        }
        int[] zb = new int[2];
        view.getLocationOnScreen(zb);
        View inflate = getLayoutInflater().inflate(R.layout.item_pop_dt, null);
        popupWindow = new PopupWindow(inflate, -2, -2, false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, zb[0]+100, zb[1]-80);
        RelativeLayout rl_xq = inflate.findViewById(R.id.rl_xq);
        TextView tv_site = inflate.findViewById(R.id.tv_site);
        tv_site.setText(child);
        rl_xq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("load", i);
                intent.putExtra("site", child);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
    }
}
