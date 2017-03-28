package com.example.zy1584.mylearndemo.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
