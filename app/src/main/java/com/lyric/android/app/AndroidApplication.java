package com.lyric.android.app;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.lyric.android.app.common.Constants;
import com.lyric.android.app.utils.LogUtils;

/**
 * application for initialized
 * @author Lyric Gan
 */
public class AndroidApplication extends Application {
    private static AndroidApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
        mInstance = this;

        LogUtils.setDebug(Constants.DEBUG);
        Stetho.initializeWithDefaults(this);
	}

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }
}
