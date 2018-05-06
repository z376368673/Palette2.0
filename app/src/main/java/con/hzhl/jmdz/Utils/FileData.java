package con.hzhl.jmdz.Utils;

import android.content.Context;
import android.text.TextUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2018/4/27.
 */

public class FileData {
    Context context;

    public FileData(Context context) {
        this.context = context;
    }

    /**
     * 是否存在
     *
     * @param pFile
     * @return
     */
    public boolean isExist(PFile pFile) {
        String path = PrefUtils.getString(context, pFile.getName(), "");
        if (TextUtils.isEmpty(path)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 保存key
     *
     * @param key
     */
    private void saveKey(String key){
        PrefUtils.putString(context, "KeyList", key);
    }
    private String  getKey(){
        return PrefUtils.getString(context, "KeyList", "");
    }
    /**
     * 保存文件的key
     * @param pFile
     */
    private void saveKey(PFile pFile) {
        String keyList =  getKey();
        if (TextUtils.isEmpty(keyList)) {
            keyList = pFile.getName();
        } else {
            keyList = keyList + "@&@" + pFile.getName();

        }
        saveKey(keyList);
    }


    /**
     * 删除文件
     *
     * 先判断这个keyList 是否是空的
     * 再判断这个文件是否存在 ，如果不存在肯定删除不了
     *
     * @param pFile
     * @return
     */
    public boolean delectFile(PFile pFile){
        String key = pFile.getName()+"@&@";
        String keyList =  getKey();
        if (TextUtils.isEmpty(keyList))return false;
        if (!isExist(pFile))return false;
        keyList = keyList.replace(key,"");
        saveKey(keyList);
        PrefUtils.putString(context, pFile.getName(), "");
        PrefUtils.remove(context,pFile.getName());
        return true;
    }

    /**
     * 获取全部的数据
     * @return
     */
    public List<PFile> getAll(){
        List<PFile> fileList = new ArrayList<>();
        String key = PrefUtils.getString(context, "KeyList", "");
        if (!TextUtils.isEmpty(key)) {
            String[] keyList = key.split("@&@");
            for (int i = 0; i < keyList.length; i++) {
                String name = keyList[i];
                String path = PrefUtils.getString(context, name, "");
                if (!TextUtils.isEmpty(path))
                fileList.add(new PFile(name,path));
            }
        }
        return  fileList;
    }

    /**
     * 获取文件总数量
     * @return
     */
    public int getFileCount() {
        String key = PrefUtils.getString(context, "KeyList", "");
        String[] keyList = key.split("@&@");
        return keyList.length;
    }

    /**
     * 保存成功 返回true ，文件已存在返回 false
     *
     * @param pFile
     * @return
     */
    public boolean save(PFile pFile) {
        //先查询有没有这个文件名，
        if (!isExist(pFile)) {
            //如果没有，先把文件名保存在keyList里面
            saveKey(pFile);
            //再把文件信息保存进去
            PrefUtils.putString(context, pFile.getName(), pFile.getPath());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 覆盖保存
     * @param pFile
     * @return
     */
    public boolean saveAs(PFile pFile) {
            //如果文件存在 则保存
            if (isExist(pFile)){
                //再把文件信息保存进去
                PrefUtils.putString(context, pFile.getName(), pFile.getPath());
                return true;
            }else {
                return false;
            }

    }

}
