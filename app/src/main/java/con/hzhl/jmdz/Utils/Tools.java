package con.hzhl.jmdz.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by apple on 2018/4/24.
 */

public class Tools {


    /**
     * 保存图片
     *
     * @param mContext
     * @param bmp
     * @param quality
     * @return
     */
    public static String saveImage(Context mContext, Bitmap bmp, int quality) {
        if (bmp == null) {
            return null;
        }
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (appDir == null) {
            return null;
        }
        if (!appDir.exists())appDir.mkdirs();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault());
        String fileName = df.format(new Date())+".jpeg";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            FileData fileData = new FileData(mContext);
            fileData.save(new PFile(fileName,file.getAbsolutePath()));
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    /**
     * 保存图片
     *
     * @param mContext
     * @param bmp
     * @param quality
     * @return
     */
    public static String saveImage(Context mContext, String fileName ,Bitmap bmp, int quality) {
        if (bmp == null) {
            return null;
        }
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (appDir == null) {
            return null;
        }
        if (!appDir.exists())appDir.mkdirs();

        File file = new File(appDir, fileName+".jpeg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            FileData fileData = new FileData(mContext);
            fileData.saveAs(new PFile(fileName,file.getAbsolutePath()));
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 保存图片
     *
     * @param mContext
     * @param bmp
     * @param quality
     * @return
     */
    public static String savesImage(Context mContext, String fileName ,Bitmap bmp, int quality) {
        if (bmp == null) {
            return null;
        }
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (appDir == null) {
            return null;
        }
        if (!appDir.exists())appDir.mkdirs();

        File file = new File(appDir, fileName+".jpeg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            FileData fileData = new FileData(mContext);
            fileData.saves(new PFile(fileName,file.getAbsolutePath()));
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }




    /**
     * 保存分享的图片
     *
     * @param mContext
     * @param bmp
     * @param quality
     * @return
     */
    public static String buildImage(Context mContext, Bitmap bmp, int quality) {
        if (bmp == null) {
            return null;
        }
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (appDir == null) {
            return null;
        }
        if (!appDir.exists())appDir.mkdirs();
        String fileName = "share_tmp.jpeg";
        File file = new File(appDir, fileName);
        if (file.exists())file.delete();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fileName = file.getAbsolutePath();
            return fileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
    Bitmap screenshot;
    screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
    Canvas canvas = new Canvas(screenshot);
    canvas.translate(-v.getScrollX(), -v.getScrollY());
    //我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
    // v.draw(canvas);
    // 将 view 画到画布上
    return screenshot;
   }
}
