package com.hzhl.jmdz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2018/4/26.
 */

public class BackgroundListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final int DelectFile = 1;

    GridView gridView;
    TthisAdapter adapter;
    int[] listBackground = {R.drawable.iv_paint_bg_1, R.drawable.iv_paint_bg_2};

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_list);
        initView();
        initData();
    }

    /**
     * 初始化view
     */
    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new TthisAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        //权限申请
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

    }


    private void initData() {
        List<Integer> fileList = new ArrayList<>();
        for (int i = 0; i < listBackground.length; i++) {
            fileList.add(listBackground[i]);
        }
        adapter.clear();
        adapter.addAll(fileList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(mContext, DrawPenActivity.class);
        intent.putExtra("BG", listBackground[i]);
        startActivityForResult(intent, 1001);
        setResult(RESULT_OK);
        finish();
    }


    /**
     * 内部类  适配器
     */
    public class TthisAdapter extends ArrayAdapter<Integer> {

        public TthisAdapter(@NonNull Context context) {
            super(context, 0);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_list, null);
            }
            ImageView iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            Integer rid = getItem(position);
            Glide.with(mActivity)
                    .load(rid)
                    .thumbnail(0.5f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(iv_img);
            if (rid == R.drawable.iv_paint_bg_1) {
                tv_name.setText("笔记样式");
            } else if (rid == R.drawable.iv_paint_bg_1) {
                tv_name.setText("表格样式");
            }

            return convertView;
        }
    }

}
