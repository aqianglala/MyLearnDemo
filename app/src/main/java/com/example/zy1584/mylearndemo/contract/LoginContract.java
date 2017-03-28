package com.example.zy1584.mylearndemo.contract;

public class LoginContract {

    public interface LoginView {
        String getUserName();

        String getPwd();

        void loginSuccess(String str);

        void loginFail(String failMsg);
    }


    public interface LoginPresenter {
        void login(String name, String pwd);
    }
}
