package com.example.zy1584.mylearndemo.activities;

import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.widget.EditText;
import com.example.zy1584.mylearndemo.R;
import com.example.zy1584.mylearndemo.base.BaseActivity;
import com.example.zy1584.mylearndemo.contract.LoginContract;
import com.example.zy1584.mylearndemo.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.LoginView {

    @BindView(R.id.input_email) EditText inputEmail;
    @BindView(R.id.input_password) EditText inputPassword;

    @OnClick(R.id.btn_login) void login(){
        mPresenter.login(getUserName(), getPwd());
    }

    @Override
    protected LoginPresenter loadPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public String getUserName() {
        return inputEmail.getText().toString().trim();
    }

    @Override
    public String getPwd() {
        return inputPassword.getText().toString().trim();
    }

    @Override
    public void loginSuccess(String str) {
        toast(str);
    }

    @Override
    public void loginFail(String failMsg) {
        toast(failMsg);
    }


    public boolean checkNull() {
        boolean isNull = false;
        if (TextUtils.isEmpty(getUserName())) {
            inputEmail.setError("账号不能为空");
            isNull = true;
        } else if (TextUtils.isEmpty(getPwd())) {
            inputPassword.setError("密码不能为空");
            isNull = true;
        }
        return isNull;
    }

}
