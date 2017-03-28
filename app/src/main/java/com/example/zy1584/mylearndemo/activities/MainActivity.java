package com.example.zy1584.mylearndemo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zy1584.mylearndemo.R;
import com.example.zy1584.mylearndemo.base.BaseActivity;
import com.example.zy1584.mylearndemo.base.BasePresenter;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter loadPresenter() {
        return null;
    }
}
