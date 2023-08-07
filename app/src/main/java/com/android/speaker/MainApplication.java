package com.android.speaker;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.android.speaker.base.Constants;
import com.chinsion.SpeakEnglish.BuildConfig;

import java.util.List;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatformConfig;
import cn.jiguang.verifysdk.api.JVerificationInterface;

public class MainApplication extends Application {
    private static MainApplication instance;
    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        if(isMainProcess()) {
            initJiguang();
        }
    }

    private void initJiguang() {
        JVerificationInterface.init(this);
        JVerificationInterface.setDebugMode(BuildConfig.DEBUG);

        JShareInterface.setDebugMode(true);
        PlatformConfig platformConfig = new PlatformConfig()
                .setWechat(Constants.WX_APP_ID, Constants.WX_APP_SECRET);
        JShareInterface.init(this, platformConfig);
    }

    private boolean isMainProcess() {
        String mainProcessName = this.getPackageName();
        ActivityManager am = ((ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if(runningApps == null || runningApps.size() == 0) {
            return false;
        }
        for(ActivityManager.RunningAppProcessInfo info : runningApps) {
            if(mainProcessName.equals(info.processName)) {
                return true;
            }
        }

        return false;
    }
}
