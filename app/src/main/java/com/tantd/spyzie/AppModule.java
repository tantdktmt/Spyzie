package com.tantd.spyzie;

import com.tantd.spyzie.data.network.ApiHelper;
import com.tantd.spyzie.data.network.AppApiHelper;
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
    public SpyzieApplication provideApplicationContext() {
        return mApplication;
    }

    @Singleton
    @Provides
    public SchedulerProvider provideSchedulerProvider() {
        return AppSchedulerProvider.getInstance();
    }

    @Singleton
    @Provides
    public ApiHelper provideApiHelper() {
        return AppApiHelper.getInstance();
    }

    @Provides
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
