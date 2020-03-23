package com.tantd.spyzie.util.rx;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppSchedulerProvider implements SchedulerProvider {

    private static AppSchedulerProvider instance;

    public static synchronized AppSchedulerProvider getInstance() {
        if (instance == null) {
            instance = new AppSchedulerProvider();
        }
        return instance;
    }

    private AppSchedulerProvider() {
        
    }

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }
}
