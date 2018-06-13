package con.hzhl.jmdz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import con.hzhl.jmdz.Utils.BaseDialog;
import con.hzhl.jmdz.Utils.FileData;
import con.hzhl.jmdz.Utils.PFile;
import con.hzhl.jmdz.Utils.StyleDialog;
import con.hzhl.jmdz.Utils.Tools;
import con.hzhl.jmdz.paint_code.IPenConfig;
import con.hzhl.jmdz.paint_code.NewDrawPenView;

public class DrawPenActivity extends BaseActivity implements View.OnClickListener, Handler.Callback {

    private static final String TAG = "TAG";
    private NewDrawPenView mPaletteView;
    private static final int MSG_SAVE_SUCCESS = 1;
    private static final int MSG_SAVE_FAILED = 2;
    private static final int MSG_SAVE_SUCCESS_ADN_FINSH = 3;
    private static final int MSG_DELE_SUCCESS_ADN_FINSH = 4;
    private static final int MSG_SHARE = 5;
    private Handler mHandler;
    private View paint, close, share, save;

    private PFile pFile;
    private String fileName = "";
    private Bitmap currentBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            currentBitmap = savedInstanceState.getParcelable("CurrentBitmap");
        }
        hidBottomUIAndStatus();
        setContentView(R.layout.activity_draw_pen);
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
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault());
            fileName = df.format(new Date());
        }

        mPaletteView = (NewDrawPenView) findViewById(R.id.palette);
        //如果是编辑修改某个界面，则加载此界面的文件
        if (pFile != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(pFile.getPath());
            Drawable drawable = new BitmapDrawable(bitmap);
            mPaletteView.setBackground(drawable);
        } else {
            //如果是新建的界面，则根据选择的背景创建界面
            int bg = getIntent().getIntExtra("BG", R.drawable.iv_paint_bg_1);
            mPaletteView.setBackgroundResource(bg);
        }
            // 因为分享导致屏幕旋转 取出旋转之前的界面 复制给本view
        if (currentBitmap != null) {
            Drawable drawable = new BitmapDrawable(currentBitmap);
            mPaletteView.setBackground(drawable);
        }

        paint = findViewById(R.id.iv_paint);
        close = findViewById(R.id.iv_close);
        share = findViewById(R.id.iv_share);
        save = findViewById(R.id.iv_save);


        paint.setOnClickListener(this);
        close.setOnClickListener(this);
        share.setOnClickListener(this);
        save.setOnClickListener(this);
        share.setOnLongClickListener(this);

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
            case MSG_SHARE: //
                String path = (String) msg.obj;
                //shareDialog(new File(path));
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("image/*");
                imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
                startActivity(Intent.createChooser(imageIntent, "分享"));
                break;
            case 1001: //设置笔的颜色
                String color = (String) msg.obj;
                mPaletteView.setPaintColor(Color.parseColor(color));
                break;
            case 1002: //设置笔的粗细
                int size = (int) msg.obj;
                mPaletteView.setPaintWidth(size);
                break;
        }
        return true;
    }


    /**
     * 保存文件
     *
     * @param type
     */
    private void saveFile(final int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 保存图片有问题 saveas 这里有问题
                Bitmap bm = mPaletteView.buildBitmap();
                currentBitmap = bm;
                if (pFile == null) {
                    Tools.savesImage(mContext, fileName, bm, 100);
                } else {
                    Tools.savesImage(mContext, pFile.getName(), bm, 100);
                }
                mHandler.sendEmptyMessage(type);
            }
        }).start();
    }

    /**
     * 退出保存文件 需要延迟保存，不然dialog的存在会导致界面变形
     *
     * @param type
     */
    private void saveFile1(final int type) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 保存图片有问题 saveas 这里有问题
                Bitmap bm = mPaletteView.buildBitmap();
                currentBitmap = bm;
                if (pFile == null) {
                    Tools.savesImage(mContext, fileName, bm, 100);
                } else {
                    Tools.savesImage(mContext, pFile.getName(), bm, 100);
                }
                mHandler.sendEmptyMessage(type);
            }
        }, 1500);

    }

    @Override
    public void onBackPressed() {
        BaseDialog.dialogStyle1(mContext, "是否要保存此编辑？", "保存并退出", "退出", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.dialog_confirm) {
                    saveFile1(MSG_SAVE_SUCCESS_ADN_FINSH);
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        //super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.clear: //清除
                mPaletteView.setCanvasCode(IPenConfig.STROKE_TYPE_ERASER);
                mPaletteView.setCanvasCode(IPenConfig.STROKE_TYPE_PEN);
                break;
            case R.id.iv_save://保存
                saveFile(MSG_SAVE_SUCCESS);
                break;
            case R.id.iv_share: //分享
                //异步生成图片，弹出分享框
               // saveFile(MSG_SAVE_SUCCESS);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bm = mPaletteView.buildBitmap();
                        currentBitmap = bm;
                        String path = Tools.buildImage(mContext, bm, 50);
                        mHandler.obtainMessage(MSG_SHARE, path).sendToTarget();
                    }
                }).start();
                break;
            case R.id.iv_paint://设置
                StyleDialog styleDialog = new StyleDialog(this);
                styleDialog.setHandler(mHandler);
                styleDialog.setSeekBar(IPenConfig.PEN_WIDTH);
                styleDialog.show();
                break;
            case R.id.iv_close://关闭
                BaseDialog.dialogStyle1(mContext, "是否要保存此编辑？", "保存并退出", "退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.dialog_confirm) {
                            saveFile1(MSG_SAVE_SUCCESS_ADN_FINSH);
                        } else {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
                break;
        }
    }

    //分享导致的屏幕旋转，所以保存分享之前的图片
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("SharePath", sharePath);
        outState.putParcelable("CurrentBitmap", currentBitmap);
        Log.e(TAG, "onSaveInstanceState: currentBitmap = " + currentBitmap.getByteCount());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_SAVE_FAILED);
        mHandler.removeMessages(MSG_SAVE_SUCCESS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
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
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE | View.INVISIBLE;
        params.type = WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL | WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        _window.setAttributes(params);
    }

}
