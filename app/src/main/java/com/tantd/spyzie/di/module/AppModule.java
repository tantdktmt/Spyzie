package com.tantd.spyzie.di.module;

import android.content.Context;

import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.core.SingleClick;
import com.tantd.spyzie.core.SingleItemClick;
import com.tantd.spyzie.data.network.ApiHelper;
import com.tantd.spyzie.data.network.AppApiHelper;
import com.tantd.spyzie.di.ApplicationContext;
import com.tantd.spyzie.util.rx.AppSchedulerProvider;
import com.tantd.spyzie.util.rx.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class AppModule {

    private SpyzieApplication mApplication;

    public AppModule(SpyzieApplication application) {
        mApplication = application;
    }

    @Singleton
    @Provides
    public SchedulerProvider provideSchedulerProvider() {
        return AppSchedulerProvider.getInstance();
    }

    @Provides
    @ApplicationContext
    public Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Singleton
    @Provides
    public ApiHelper provideApiHelper() {
        return AppApiHelper.getInstance();
    }

    @Provides
    public SingleClick provideSingleClick() {
        return new SingleClick();
    }

    @Provides
    public SingleItemClick provideSingleItemClick() {
        return new SingleItemClick();
    }
}
