package con.hzhl.jmdz;

import android.app.Application;
import android.util.Config;
import android.util.Log;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.common.SocializeConstants;

/**
 * Created by apple on 2018/5/4.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashCatchHandler.getInstance().init(this);
        UMConfigure.init(this,"5aebcce8f29d981c2f0000d3"
                ,"HaoShare",UMConfigure.DEVICE_TYPE_PHONE,"");
        PlatformConfig.setWeixin("wxcbb0f8a815203783", "5aebcce8f29d981c2f0000d3");
        UMShareAPI.get(this);
        Log.e("友盟SDK_VERSION", SocializeConstants.SDK_VERSION+"");
    }
}
