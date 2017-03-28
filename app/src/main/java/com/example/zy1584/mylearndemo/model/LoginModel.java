package com.example.zy1584.mylearndemo.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.zy1584.mylearndemo.base.BaseApplication;
import com.example.zy1584.mylearndemo.base.BaseModel;
import com.example.zy1584.mylearndemo.bean.LoginBean;
import com.example.zy1584.mylearndemo.exception.ApiException;
import com.example.zy1584.mylearndemo.subscriber.CommonSubscriber;
import com.example.zy1584.mylearndemo.transformer.CommonTransformer;
import com.trello.rxlifecycle.LifecycleProvider;


public class LoginModel extends BaseModel {
    private boolean isLogin = false;

    public boolean login(Context context, LifecycleProvider provider, @NonNull String username, @NonNull String pwd, @NonNull final InfoHint
            infoHint) {

        if (infoHint == null)
            throw new RuntimeException("InfoHint不能为空");

        httpService.login(username, pwd)
                .compose(new CommonTransformer<LoginBean>(provider))
                .subscribe(new CommonSubscriber<LoginBean>(context) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        isLogin = true;
                        infoHint.successInfo(loginBean.getToken());
                    }

                    @Override
                    protected void onError(ApiException e) {
                        super.onError(e);
                        isLogin = false;
                        infoHint.failInfo(e.message);
                    }
                });
        return isLogin;
    }


    //通过接口产生信息回调
    public interface InfoHint {
        void successInfo(String str);

        void failInfo(String str);

    }

}
