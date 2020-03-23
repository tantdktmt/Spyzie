package com.tantd.spyzie;

import com.tantd.spyzie.ui.schedule.ScheduleContract;
import com.tantd.spyzie.ui.schedule.SchedulePresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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

    @Provides
    public ScheduleContract.Presenter provideSchedulePresenter() {
        return new SchedulePresenter();
    }
}
