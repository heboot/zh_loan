package com.zh.loan.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import com.waw.hr.mutils.rxbus.RxBus;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zh.loan.utils.SignUtils;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public  abstract class BaseActivity<T extends ViewDataBinding> extends FragmentActivity implements BaseUIInterface{

    protected String TAG = this.getClass().getName();

    protected T binding;

    private CompositeDisposable compositeDisposable;

    protected Map<String, Object> params;

    protected Observable<Object> rxObservable;

    public int pageSize = 15, sp = 1, total;

    protected QMUITipDialog tipDialog;

    protected QMUITipDialog loadingDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        params = SignUtils.getNormalParams();
        initContentView();
        initUI();
        initData();
        rxBusObservers();
        initListener();
    }

    protected void initContentView() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    @LayoutRes
    protected abstract int getLayoutId();

    public void addDisposable(Disposable disposable) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }
        this.compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {


        if (this.compositeDisposable != null && !compositeDisposable.isDisposed()) {
            this.compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private void rxBusObservers() {
        rxObservable = RxBus.getInstance().toObserverable();
        rxObservable.subscribe(new Observer<Object>() {

            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
                this.disposable = d;
            }

            @Override
            public void onNext(Object o) {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
