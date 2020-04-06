package com.tantd.spyzie.di.module;

import android.app.Service;
import android.content.Context;

import com.tantd.spyzie.di.ServiceContext;
import com.tantd.spyzie.di.ServiceScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @ServiceContext
    @ServiceScope
    public Context provideContext() {
        return mService;
    }
}
