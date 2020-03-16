package com.lenovo.smarttraffic.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.bean.Car;
import com.lenovo.smarttraffic.bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterInputStream;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class ViolationActivity extends BaseActivity {

    private TextView tv_2;
    private TextView tv_1;
    private TextView tv_3;
    private ImageView im_1;
    private User.ROWSDETAILBean user;
    private JSONArray violations;
    private ListView lv_list;
    @SuppressLint("HandlerLeak")
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ViolationAdapter violationAdapter = new ViolationAdapter();
            lv_list.setAdapter(violationAdapter);

        }
    };
    private TextView tv_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_violation;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, "我的违章");
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        im_1 = findViewById(R.id.im_1);
        tv_text = findViewById(R.id.tv_text);
        lv_list = findViewById(R.id.lv_list);

        tv_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_text.getVisibility() != View.GONE) {
                    tv_text.setVisibility(View.GONE);
                }
            }
        });
    }

    private void InitData() {
        user = InitApp.getUser("pcardid", InitApp.sp.getString("vuserinfo", ""));
        tv_1.setText("姓名："+user.getPname());
        tv_2.setText("性别："+user.getPsex());
        tv_3.setText("手机号码："+user.getPtel());
        if (user.getPsex().equals("男")) {
            im_1.setImageResource(R.mipmap.touxiang_2);
        } else {
            im_1.setImageResource(R.mipmap.touxiang_1);

        }
        String str = InitApp.sp.getString(user.getUsername(), "");
        try {

            if (str.equals("")) {
                List<Car.ROWSDETAILBean> carInfo = new Gson().fromJson(InitApp.sp.getString("carInfo", ""), Car.class).getROWS_DETAIL();

                ArrayList<Car.ROWSDETAILBean> arrayList = new ArrayList<>();
                for (int i = 0; i < carInfo.size(); i++) {
                    if (carInfo.get(i).getPcardid().equals(user.getPcardid())) {
                        arrayList.add(carInfo.get(i));
                    }
                }
                violations = new JSONArray();
                JSONArray jsonArray = new JSONArray(InitApp.sp.getString("violations", ""));
                for (int i = 0; i < arrayList.size(); i++) {
                    for (int j = 0; j < jsonArray.length(); j++) {
                        if (arrayList.get(i).getCarnumber().equals(jsonArray.getJSONObject(j).getString("carnumber"))) {
                            jsonArray.getJSONObject(j).put("pcarband", carInfo.get(i).getCarbrand());
                            violations.put(jsonArray.getJSONObject(j));
                        }
                    }
                }
                InitApp.edit.putString(user.getUsername(), violations.toString()).commit();
                mHander.sendEmptyMessage(0);
            } else {
                violations = new JSONArray(str);
                mHander.sendEmptyMessage(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        InitData();
        super.onResume();
    }

    private class ViolationAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return violations.length();
        }

        @Override
        public JSONObject getItem(int i) {
            try {
                return violations.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View conview = null;
            if (view == null) {
                conview = View.inflate(getApplicationContext(), R.layout.violation_list, null);
            } else {
                conview = view;
            }
            TextView tv_1 = conview.findViewById(R.id.tv_1);
            TextView tv_2 = conview.findViewById(R.id.tv_2);
            TextView tv_3 = conview.findViewById(R.id.tv_3);
            TextView tv_4 = conview.findViewById(R.id.tv_4);
            TextView tv_5 = conview.findViewById(R.id.tv_5);
            TextView tv_6 = conview.findViewById(R.id.tv_6);
            TextView tv_7 = conview.findViewById(R.id.tv_7);
            TextView tv_8 = conview.findViewById(R.id.tv_8);
            ImageView im_1 = conview.findViewById(R.id.im_1);

            tv_1.setText(i + 1 + "");

            try {
                int icon = getResources().getIdentifier(getItem(i).getString("pcarband"), "mipmap", getPackageName());
                im_1.setImageResource(icon);
                tv_2.setText(getItem(i).getString("carnumber"));
                tv_3.setText(getItem(i).getString("paddr"));
                String xx = getItem(i).getString("premarks");
                tv_4.setText(xx.replaceAll(xx.substring(2, xx.length() - 3), "..."));
                tv_5.setText(getItem(i).getString("pscore"));
                tv_6.setText(getItem(i).getString("pmoney"));
                tv_7.setText(getItem(i).getString("pdatetime"));
                if (getItem(i).getInt("pchuli") == 0) {
                    tv_8.setText("未处理");
                    tv_8.setTextColor(Color.RED);
                } else {
                    tv_8.setText("已处理");
                    tv_8.setTextColor(Color.GREEN);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            tv_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        tv_text.setVisibility(View.VISIBLE);
                        tv_text.setText(getItem(i).getString("premarks"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            tv_8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (getItem(i).getInt("pchuli") != 1) {
                            Intent intent = new Intent(getApplicationContext(), ViolaionPayActivity.class);
                            intent.putExtra("u", user.getUsername());
                            intent.putExtra("p", i);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return conview;
        }
    }


}
