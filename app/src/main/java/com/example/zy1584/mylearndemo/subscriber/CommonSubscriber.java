package com.example.zy1584.mylearndemo.subscriber;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.zy1584.mylearndemo.base.BaseSubscriber;
import com.example.zy1584.mylearndemo.exception.ApiException;
import com.example.zy1584.mylearndemo.utils.NetUtils;
import com.orhanobut.logger.Logger;


/**
 * Created by tzqiang on 2017/03/28.
 */

public abstract class CommonSubscriber<T> extends BaseSubscriber<T> {

    private Context context;
    /*加载框可自己定义*/
    private ProgressDialog pd;
    /*是否弹框*/
    private boolean isShowProgress = true;
    /*弹框是否能取消*/
    private boolean isCancelable = true;

    public CommonSubscriber(Context context) {
        this.context = context;
        initProgressDialog(isCancelable);
    }

    public CommonSubscriber(Context context, boolean isShowProgress, boolean isCancelable) {
        this.context = context;
        this.isShowProgress = isShowProgress;
        this.isCancelable = isCancelable;
        if (isShowProgress){
            initProgressDialog(isCancelable);
        }
    }

    private static final String TAG = "CommonSubscriber";

    @Override
    public void onStart() {
        showProgressDialog();
        if (!NetUtils.isConnected(context)) {
            Logger.t(TAG).e("网络不可用");
        } else {
            Logger.t(TAG).e("网络可用");
        }
    }



    @Override
    protected void onError(ApiException e) {
        dismissProgressDialog();
        Logger.t(TAG).e("错误信息为 " + "code:" + e.code + "   message:" + e.message);
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
        Logger.t(TAG).e("成功了");
    }

    /**
     * 初始化加载框
     */
    private void initProgressDialog(boolean cancel) {
        if (pd == null && context != null) {
            pd = new ProgressDialog(context);
            pd.setCancelable(cancel);
            if (cancel) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        onCancelProgress();
                    }
                });
            }
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }


    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        if (!isShowProgress) return;
        if (pd == null || context == null) return;
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (!isShowProgress) return;
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

}
