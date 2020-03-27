package com.tantd.spyzie.core;

import com.tantd.spyzie.data.network.ApiManager;
import com.tantd.spyzie.util.rx.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by tantd on 2/7/2020.
 */
public abstract class BasePresenter<V extends IView> implements IPresenter<V> {

    private static final String TAG = BasePresenter.class.getName();

    protected V view;
    protected final SchedulerProvider schedulerProvider;
    protected final ApiManager mApiManager;
    protected CompositeDisposable compositeDisposable;

    public BasePresenter(SchedulerProvider schedulerProvider, ApiManager apiManager, CompositeDisposable compositeDisposable) {
        this.schedulerProvider = schedulerProvider;
        this.mApiManager = apiManager;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        view = null;
        compositeDisposable.dispose();
        compositeDisposable = null;
    }

    @Override
    public void handleApiError(Throwable error) {
        view.onError(error);
    }
}
