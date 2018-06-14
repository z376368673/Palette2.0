package com.hzhl.jmdz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by apple on 2018/4/26.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {
    public Activity mActivity;
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;

    }

    public void startAct(Class cla){
        Intent intent = new Intent(this,cla);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }
}
