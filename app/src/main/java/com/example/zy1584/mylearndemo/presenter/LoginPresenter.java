package com.example.zy1584.mylearndemo.presenter;


import com.example.zy1584.mylearndemo.activities.LoginActivity;
import com.example.zy1584.mylearndemo.base.BasePresenter;
import com.example.zy1584.mylearndemo.contract.LoginContract;
import com.example.zy1584.mylearndemo.model.LoginModel;
import com.example.zy1584.mylearndemo.mvp.IModel;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

/**
 * Created by GaoSheng on 2016/11/26.
 * 20:51
 *
 * @VERSION V1.4
 * com.example.gs.mvpdemo.presenter
 */

public class LoginPresenter extends BasePresenter<LoginActivity> implements
        LoginContract.LoginPresenter {

    @Override
    public void login(String name, String pwd) {
        if (!getIView().checkNull()) {
            ((LoginModel) getiModelMap().get("login")).login(getIView(), getIView(), name, pwd, new LoginModel
                    .InfoHint() {
                @Override
                public void successInfo(String str) {
                    getIView().loginSuccess(str);  //成功
                }

                @Override
                public void failInfo(String str) {
                    Logger.e(str);

                    getIView().loginFail(str);  //失败
                }
            });
        }
    }


    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new LoginModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("login", models[0]);
        return map;
    }
}
