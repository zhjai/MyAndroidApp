package com.example.exam;

import android.app.Application;

import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TencentMapInitializer.setAgreePrivacy(true);
    }
}