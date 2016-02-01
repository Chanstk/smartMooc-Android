package com.example.lenovo.facedetector;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by lenovo on 2015/11/25.
 */
public class MyLeanCloudApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "HfdPfTKNcsOgSwU0BoQ8RDye", "qxCIFYgN7nbQJfm8eEGiwIer");
    }
}
