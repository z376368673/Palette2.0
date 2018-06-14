package com.hzhl.jmdz;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import com.hzhl.jmdz.Utils.BaseDialog;
import com.hzhl.jmdz.Utils.FileData;
import com.hzhl.jmdz.Utils.PFile;

/**
 * Created by apple on 2018/4/26.
 */

public class CollectionListActivity extends BaseActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private static final int DelectFile = 1;

    GridView gridView;
    CollectAdapter adapter;
    ImageView iv_add;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();
        initData();
    }


    /**
     * 初始化view
     */
    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        adapter = new CollectAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
        iv_add.setOnClickListener(this);
        //权限申请
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {

    }

    /**
     * 获取文件存储的信息 以后最好改进为 扫描文件获取信息
     */
    private void initData() {
        FileData fileData = new FileData(this);
        List<PFile> fileList = fileData.getAll();
        adapter.clear();
        adapter.addAll(fileList);
        adapter.notifyDataSetChanged();
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (iv_add == view) {
            //startAct(PaletteActivity.class);
            Intent intent = new Intent(mContext, BackgroundListActivity.class);
            startActivityForResult(intent, 1001);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            initData();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PFile file = (PFile) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent(mContext, DrawPenActivity.class);
        intent.putExtra("name", file.getName());
        intent.putExtra("path", file.getPath());
        startActivityForResult(intent, 1001);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final PFile pFile = (PFile) adapterView.getAdapter().getItem(i);
        if (pFile!=null){
            BaseDialog.dialogStyle1(mContext, "您确定要删除此文件？", "删除", "取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId()==R.id.dialog_confirm) {
                        delect(pFile);
                    }
                }
            });


        }
        return true;
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==DelectFile){
                initData();
            }
        }
    };
    private void delect(final PFile pFile){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isdelect = new FileData(mContext).delectFile(pFile);
                if (isdelect)
                    handler.sendEmptyMessage(DelectFile);
                else {
                    Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    /**
     * 内部类  适配器
     */
    public class CollectAdapter extends ArrayAdapter<PFile> {

        public CollectAdapter(@NonNull Context context) {
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
            PFile file = getItem(position);
            Glide.with(mActivity)
                    .load(file.getPath())
                    .thumbnail(0.5f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(iv_img);
            tv_name.setText(file.getName());
            return convertView;
        }
    }

}
