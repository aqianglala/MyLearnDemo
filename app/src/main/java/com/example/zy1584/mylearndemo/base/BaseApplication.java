package com.example.zy1584.mylearndemo.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

public class BaseApplication extends Application{

    private static Context context;
    private static Thread mainThread;
    private static long mainThreadId;
    private static Handler mainHandler;
    private static Looper mainlooper;
    private String TAG = "base_tag";

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        //获取主线程
        mainThread=Thread.currentThread();
        //获取当前的线程id
        mainThreadId=android.os.Process.myTid();
        //在主线程初始化一个全局的handler。
        mainHandler=new Handler();

        mainlooper=getMainLooper();

//        LeakCanary.install(this);
        Logger.init(TAG)                 // default PRETTYLOGGER or use just init()
                .logLevel(LogLevel.FULL) ;       // default LogLevel.FULL
    }

    public static Context getContext() {
        return context;
    }

    public static Thread getMainThread() {
        return mainThread;
    }

    public static long getMainThreadId() {
        return mainThreadId;
    }

    public static Handler getMainHandler() {
        return mainHandler;
    }

    public static Looper getMainlooper() {
        return mainlooper;
    }

}
