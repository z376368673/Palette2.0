package com.hzhl.jmdz;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by apple on 2018/4/26.
 */

public class WelcomeActivity extends BaseActivity {


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
         startAct(CollectionListActivity.class);
         finish();

    }

}
