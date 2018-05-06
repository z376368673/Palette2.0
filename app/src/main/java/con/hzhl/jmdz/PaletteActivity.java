package con.hzhl.jmdz;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import con.hzhl.jmdz.Utils.BaseDialog;
import con.hzhl.jmdz.Utils.FileData;
import con.hzhl.jmdz.Utils.PFile;
import con.hzhl.jmdz.Utils.StyleDialog;
import con.hzhl.jmdz.Utils.Tools;

import java.io.File;

public class PaletteActivity extends BaseActivity implements View.OnClickListener, PaletteView.Callback, Handler.Callback {

    private View mUndoView;
    private View mRedoView;
    private View mPenView;
    private View mEraserView;
    private View mClearView;
    private PaletteView mPaletteView;
    private static final int MSG_SAVE_SUCCESS = 1;
    private static final int MSG_SAVE_FAILED = 2;
    private static final int MSG_SAVE_SUCCESS_ADN_FINSH = 3;
    private static final int MSG_DELE_SUCCESS_ADN_FINSH = 4;
    private static final int MSG_SHARE = 5;
    private Handler mHandler;
    private View smaller, close, share, save;

    private PFile pFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hidBottomUIAndStatus();
        setContentView(R.layout.activity_palettr);
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        String name = getIntent().getStringExtra("name");
        String path = getIntent().getStringExtra("path");
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(path)) {
            pFile = new PFile(name, path);
        }

        mPaletteView = (PaletteView) findViewById(R.id.palette);
        if (pFile != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(pFile.getPath());
            Drawable drawable = new BitmapDrawable(bitmap);
            mPaletteView.setBackground(drawable);
        }
        mPaletteView.setCallback(this);

        mUndoView = findViewById(R.id.undo);
        mRedoView = findViewById(R.id.redo);
        mPenView = findViewById(R.id.pen);
        mPenView.setSelected(true);
        mEraserView = findViewById(R.id.eraser);
        mClearView = findViewById(R.id.clear);


        mUndoView.setOnClickListener(this);
        mRedoView.setOnClickListener(this);
        mPenView.setOnClickListener(this);
        mEraserView.setOnClickListener(this);
        mClearView.setOnClickListener(this);


        smaller = findViewById(R.id.iv_smaller);
        close = findViewById(R.id.iv_close);
        share = findViewById(R.id.iv_share);
        save = findViewById(R.id.iv_save);


        smaller.setOnClickListener(this);
        close.setOnClickListener(this);
        share.setOnClickListener(this);
        save.setOnClickListener(this);

        share.setOnLongClickListener(this);

        mUndoView.setEnabled(false);
        mRedoView.setEnabled(false);

        mHandler = new Handler(this);
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SAVE_FAILED:
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
                break;
            case MSG_SAVE_SUCCESS:
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                break;
            case MSG_SAVE_SUCCESS_ADN_FINSH://保存成功 退出
                //Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
                case MSG_DELE_SUCCESS_ADN_FINSH://删除成功 退出
                //Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
                break;
            case MSG_SHARE: //设置笔的粗细
                String path  = (String) msg.obj;
                shareDialog(new File(path));
                break;
            case 1001: //设置笔的颜色
                String color = (String) msg.obj;
                mPaletteView.setPenColor(Color.parseColor(color));
                break;
            case 1002: //设置笔的粗细
                int size = (int) msg.obj;
                mPaletteView.setPenRawSize(size);
                break;
        }
        return true;
    }

    /**
     * 保存文件
     * @param type
     */
    private void saveFile(final int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bm = mPaletteView.buildBitmap();
                if (pFile == null) {
                    Tools.saveImage(mContext, bm, 100);
                } else {
                    Tools.saveImage(mContext, pFile.getName(), bm, 100);
                }
                mHandler.sendEmptyMessage(type);
            }
        }).start();
    }


    @Override
    public void onUndoRedoStatusChanged() {
        //以前的撤销按钮
        mUndoView.setEnabled(mPaletteView.canUndo());
        mRedoView.setEnabled(mPaletteView.canRedo());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.undo://撤销
                mPaletteView.undo();
                break;
            case R.id.redo://反撤销
                mPaletteView.redo();
                break;
            case R.id.pen://画笔动作
                v.setSelected(true);
                mEraserView.setSelected(false);
                mPaletteView.setMode(PaletteView.Mode.DRAW);
                break;
            case R.id.eraser://擦除动作
                v.setSelected(true);
                mPenView.setSelected(false);
                mPaletteView.setMode(PaletteView.Mode.ERASER);
                break;
            case R.id.clear: //清除
                mPaletteView.clear();
                break;
            case R.id.iv_save://保存
                saveFile(MSG_SAVE_SUCCESS);
//                BaseDialog.dialogStyle1(mContext, "您要保存此笔记？", "确定", "取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (view.getId()==R.id.dialog_confirm) {
//                                saveFile(MSG_SAVE_SUCCESS);
//                        }
//                    }
//                });

                break;
            case R.id.iv_share: //分享
                StyleDialog styleDialog = new StyleDialog(this);
                styleDialog.setHandler(mHandler);
                styleDialog.setSeekBar((int) mPaletteView.getPenRawSize());
                styleDialog.show();
                break;
            case R.id.iv_smaller://最小化
                saveFile(MSG_SAVE_SUCCESS_ADN_FINSH);
                break;
            case R.id.iv_close://关闭
                setResult(RESULT_OK);
                finish();
//                BaseDialog.dialogStyle1(mContext, "您要删除此笔记？", "确定", "取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (view.getId()==R.id.dialog_confirm) {
//                            if (pFile!=null){
//                                finish();
//                            }else {
//                                finish();
//                            }
//
//                        }
//                    }
//                });
                break;
        }
    }


    @Override
    public boolean onLongClick(View view) {
        if (view==share){
            //异步生成图片，弹出分享框
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bm = mPaletteView.buildBitmap();
                    String path = Tools.saveImage(mContext, bm, 50);
                    mHandler.obtainMessage(MSG_SHARE,path).sendToTarget();
                }
            }).start();
            return true;
        }
        return false;
    }


    /**
     * 分享
     *
     */
    public  void shareDialog( File file) {
        final UMImage image = new UMImage(mActivity, file);//本地文件
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_share, null, false);
        TextView wechat1 = (TextView) view.findViewById(R.id.wechat1);
        final TextView wechat2 = (TextView) view.findViewById(R.id.wechat2);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (view==wechat2){
                    //new ShareAction(mActivity).withText("好笔记").withMedia(image).share();
                    new ShareAction(mActivity).setCallback(shareListener).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withMedia(image).share();
                }else {
                    new ShareAction(mActivity).setCallback(shareListener).setPlatform(SHARE_MEDIA.WEIXIN).withMedia(image).share();
                }
            }
        };
        wechat1.setOnClickListener(onClickListener);
        wechat2.setOnClickListener(onClickListener);
    }

    private void delect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isdelect = new FileData(mContext).delectFile(pFile);
                if (isdelect)
                    mHandler.sendEmptyMessage(MSG_DELE_SUCCESS_ADN_FINSH);
                else {
                    Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_SAVE_FAILED);
        mHandler.removeMessages(MSG_SAVE_SUCCESS);
    }

    //隐藏虚拟按键，并且全屏
    protected void hidBottomUIAndStatus() {
        //去除title
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window _window = getWindow();
        //去掉Activity上面的状态栏
        _window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉虚拟按键全屏显示
        WindowManager.LayoutParams params = _window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE| View.INVISIBLE ;
        params.type = WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL|WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        _window.setAttributes(params);
    }
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(mActivity,"分享成功",Toast.LENGTH_LONG).show();
        }
        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mActivity,"分享失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }
        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity,"分享取消",Toast.LENGTH_LONG).show();
        }
    };

}
