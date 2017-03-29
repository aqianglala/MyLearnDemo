package com.example.zy1584.mylearndemo.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zy1584.mylearndemo.R;
import com.example.zy1584.mylearndemo.mvp.IView;
import com.example.zy1584.mylearndemo.utils.ActivityCollector;
import com.example.zy1584.mylearndemo.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by zy1584 on 2017-3-27.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements
        IView, LifecycleProvider<ActivityEvent> {// 自定义RXActivity
    protected View view;
    protected P mPresenter;

    protected String TAG;
    protected LayoutInflater mInflater;
    protected Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);// 自定义RXActivity
        setContentView(getLayoutId());
        mPresenter = loadPresenter();
        initCommonData();

        ActivityCollector.addActivity(this);
        initPublicMembers();// 初始化公共成员
        initSystemBar();// 4.4以上沉浸式状态栏

        ButterKnife.bind(this);
        initView();// 因为使用了butterKnife，所以这个方法将很少使用，因此这里不作为抽象方法。
        initListener();// 同上
        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        doBusiness();// 需要时可以复写
    }

    protected void handleIntent(Intent intent) {}

    protected void doBusiness(){};

    protected void initListener(){};

    protected void initView(){};

    protected abstract int getLayoutId();

    public interface OnRightClickListener {
        void rightClick();
    }

    protected abstract P loadPresenter();

    private void initCommonData() {

        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    private void initPublicMembers() {
        TAG = this.getClass().getSimpleName();
        mInflater = getLayoutInflater();
        activity = this;
    }

    /**
     * 对应的Activity的根布局需要继承style = "Activity"
     */
    private void initSystemBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 统一初始化titlebar
     */
    protected Toolbar initToolBar(String title) {
        ImageView ivBack = (ImageView) findViewById(R.id.toolbar_back);
        TextView tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return toolbar;
    }

    /**
     * 统一初始化titlebar右侧图片
     */
    protected Toolbar initToolBarRightImg(String title, int rightId, final OnRightClickListener listener) {
        ImageView ivBack = (ImageView) findViewById(R.id.toolbar_back);
        TextView tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView ivRight = (ImageView) findViewById(R.id.toolbar_iv_menu);
        ivRight.setImageResource(rightId);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });
        return toolbar;
    }


    /**
     * 统一初始化titlebar右侧文字
     */
    protected Toolbar initToolBarRightTxt(String title, String right, final OnRightClickListener listener) {
        ImageView ivBack = (ImageView) findViewById(R.id.toolbar_back);
        TextView tvTitle = (TextView) findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvRight = (TextView) findViewById(R.id.toolbar_tv_menu);
        tvRight.setText(right);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });
        return toolbar;
    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(this, clz);
        if (ex != null) intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            finish();
        }
    }

    /**
     * @param str 显示一个内容为str的toast
     */
    public void toast(String str) {
        ToastUtils.showShort(this, str);
    }

    /**
     * @param contentId 显示一个内容为contentId指定的toast
     */
    public void toast(int contentId) {
        ToastUtils.showShort(this, contentId);
    }

    /**
     * @param str 日志的处理
     */
    public void LogI(String str) {
        Logger.t(TAG).i(str);
    }

    /******************************************* Android 6.0权限封装 ***********************************************/

    public static  int REQUEST_CODE = 0;

    public void requestPermission(String[] permissions,int requestCode)
    {

        this.REQUEST_CODE = requestCode;

        //检查权限是否授权
        if(checkPermissions(permissions))
        {
            permissionSucceed(REQUEST_CODE);
        }
        else
        {
            List<String> needPermissions = getPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE);
        }
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions)
    {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
        {
            return true;
        }

        for(String permission:permissions)
        {

            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }

    private List<String> getPermissions(String[] permissions)
    {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_CODE) {
            if (verificationPermissions(grantResults)) {
                permissionSucceed(REQUEST_CODE);
            } else {
                permissionFailing(REQUEST_CODE);
                showFailingDialog();
            }
        }
    }

    private boolean verificationPermissions(int[] results)
    {

        for (int result : results) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;

    }

    private void showFailingDialog()
    {

        new AlertDialog.Builder(this)
                .setTitle("消息")
                .setMessage("当前应用无此权限，该功能暂时无法使用。如若需要，请单击确定按钮进行权限授权！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        return;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSettings();
                    }
                }).show();

    }

    private void startSettings() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    public void permissionFailing(int code) {

        Log.d(TAG, "获取权限失败=" + code);
    }

    public void permissionSucceed(int code) {

        Log.d(TAG, "获取权限成功=" + code);
    }

    /******************************************* RXActivity ***********************************************/

    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);// activity管理
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }
}
