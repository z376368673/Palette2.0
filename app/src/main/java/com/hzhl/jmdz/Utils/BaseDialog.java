package com.hzhl.jmdz.Utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hzhl.jmdz.R;


/**
 * Created by apple on 2018/4/28.
 */

public class BaseDialog {


    /**
     * 确认取消
     *
     * @param context
     * @param info
     */
    public static void dialogStyle1(final Context context, String info, String confirm, String cancel, final View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_style1, null, false);
        TextView tv_content = (TextView) view.findViewById(R.id.dialog_content);
        tv_content.setText(info);
        TextView tv_confirm = (TextView) view.findViewById(R.id.dialog_confirm);
        tv_confirm.setText(confirm);
        TextView tv_cancel = (TextView) view.findViewById(R.id.dialog_cancel);
        tv_cancel.setText(cancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        setDialog(dialog);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v);
                dialog.dismiss();

            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v);
                dialog.dismiss();
            }
        });
    }






    /**
     * 统一设置dialog宽度
     *
     * @param dialog
     */
    private static void setDialog(AlertDialog dialog) {
        WindowManager m = dialog.getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = d.getWidth() / 5 * 4; //设置dialog的宽度为当前手机屏幕的宽度
        dialog.getWindow().setAttributes(p);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

}
