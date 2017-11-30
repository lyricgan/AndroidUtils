package com.lyric.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.lyric.android.app.common.BaseApplication;
import com.lyric.android.app.common.Constants;
import com.lyric.android.app.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * 应用入口，进行初始化
 * @author lyricgan
 * @time 2015/10/7 14:04
 */
public class AndroidApplication extends BaseApplication {
    private static final String TAG = AndroidApplication.class.getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();

        LogUtils.setDebug(Constants.DEBUG);

        addRegisterActivityLifecycleCallbacks();
	}

    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    private void addRegisterActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                debugMessage(activity, "onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                debugMessage(activity, "onActivityStarted");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                debugMessage(activity, "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                debugMessage(activity, "onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                debugMessage(activity, "onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                debugMessage(activity, "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                debugMessage(activity, "onActivityDestroyed");
            }
        });
    }

    private void debugMessage(Activity activity, String lifecycle) {
        Log.d(TAG, "activityName:" + activity.getClass().getName() + ",lifecycle:" + lifecycle);
    }
}