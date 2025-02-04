package cn.rongcloud.im;

import androidx.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.tencent.bugly.crashreport.CrashReport;

import cn.rongcloud.im.common.ErrorCode;
import cn.rongcloud.im.contact.PhoneContactManager;
import cn.rongcloud.im.im.IMManager;
import cn.rongcloud.im.utils.SearchUtils;
import cn.rongcloud.im.wx.WXManager;
import io.rong.imlib.ipc.RongExceptionHandler;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

public class SealApp extends MultiDexApplication {
    private static SealApp appInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;

        // 初始化 bugly BUG 统计
        //CrashReport.initCrashReport(getApplicationContext());

        ErrorCode.init(this);

        /*
         * 以上部分在所有进程中会执行
         */
        if (!getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            return;
        }
        /*
         * 以下部分仅在主进程中进行执行
         */
        // 初始化融云IM SDK，初始化 SDK 仅需要在主进程中初始化一次
        IMManager.getInstance().init(this);
        Stetho.initializeWithDefaults(this);

        SearchUtils.init(this);

        Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

        // 微信分享初始化
        WXManager.getInstance().init(this);

        PhoneContactManager.getInstance().init(this);
    }

    public static SealApp getApplication(){
        return appInstance;
    }

}
