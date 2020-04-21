package com.tantd.spyzie.di.module;

import android.content.Context;

import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.core.SingleClick;
import com.tantd.spyzie.core.SingleItemClick;
import com.tantd.spyzie.data.db.AppDbManager;
import com.tantd.spyzie.data.db.DbManager;
import com.tantd.spyzie.data.network.ApiManager;
import com.tantd.spyzie.data.network.AppApiManager;
import com.tantd.spyzie.di.ApplicationContext;
import com.tantd.spyzie.util.JsonUtil;
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
    public ApiManager provideApiHelper() {
        return AppApiManager.getInstance();
    }

    @Singleton
    @Provides
    public DbManager provideDbManager() {
        return AppDbManager.getInstance();
    }

    @Provides
    public SingleClick provideSingleClick() {
        return new SingleClick();
    }

    @Provides
    public SingleItemClick provideSingleItemClick() {
        return new SingleItemClick();
    }

    @Singleton
    @Provides
    public JsonUtil providesJsonUtil() {
        return JsonUtil.getInstance();
    }
}
