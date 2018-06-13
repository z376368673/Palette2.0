package con.hzhl.jmdz.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import con.hzhl.jmdz.R;


/*
 *  创建者:     ZH
 *  创建时间:   2018/03/08  9:32
 *  描述:  
 */
public class StyleDialog extends Dialog implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {

    private LinearLayout ll_style;
    private View color1, color2, color3, color4, color5, color6;
    private SeekBar seekBar;
    private int progress = 0;
    private Handler handler;
    Context context;

    public StyleDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public StyleDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected StyleDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setContentView(R.layout.dialog_style);
        ll_style = (LinearLayout) findViewById(R.id.ll_style);
        color1 = findViewById(R.id.color1);
        color2 = findViewById(R.id.color2);
        color3 = findViewById(R.id.color3);
        color4 = findViewById(R.id.color4);
        color5 = findViewById(R.id.color5);
        color6 = findViewById(R.id.color6);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(progress);

        //ll_style.setOnClickListener(this);
        color1.setOnClickListener(this);
        color2.setOnClickListener(this);
        color3.setOnClickListener(this);
        color4.setOnClickListener(this);
        color5.setOnClickListener(this);
        color6.setOnClickListener(this);
    }

    public void setSeekBar(int progress) {
        if (progress<=30){
            this.progress = (progress-10)/2;
        }else {
            this.progress = progress/3;
        }

        if (seekBar!=null)seekBar.setProgress(progress);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if (handler == null) return;
        if (v == color1 | v == color2 | v == color3 | v == color4 | v == color5 | v == color6) {
            String color = (String) v.getTag();
            handler.obtainMessage(1001, color).sendToTarget();
        } else if (v == ll_style) {

        }
        dismiss();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Log.e("SeekBar",i+"");
        handler.obtainMessage(1002, i).sendToTarget();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
