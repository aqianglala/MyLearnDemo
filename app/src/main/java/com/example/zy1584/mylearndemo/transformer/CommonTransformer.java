package com.example.zy1584.mylearndemo.transformer;


import com.example.zy1584.mylearndemo.base.BaseHttpResult;
import com.trello.rxlifecycle.LifecycleProvider;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class CommonTransformer<T> implements Observable.Transformer<BaseHttpResult<T>, T> {

    private LifecycleProvider provider;
    public CommonTransformer(LifecycleProvider provider) {
        this.provider = provider;
    }

    @Override
    public Observable<T> call(Observable<BaseHttpResult<T>> tansFormerObservable) {
        return tansFormerObservable.compose(provider.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ErrorTransformer.<T>getInstance());
    }
}

