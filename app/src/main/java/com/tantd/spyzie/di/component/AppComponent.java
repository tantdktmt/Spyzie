package com.tantd.spyzie.di.component;

import com.tantd.spyzie.MyAppWidget;
import com.tantd.spyzie.di.module.AppModule;
import com.tantd.spyzie.di.module.ServiceModule;
import com.tantd.spyzie.ui.schedule.ScheduleActivity;
import com.tantd.spyzie.ui.schedule.ScheduleFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(ScheduleActivity activity);

    void inject(ScheduleFragment fragment);

    void inject(MyAppWidget appWidget);

    ServiceComponent plus(ServiceModule module);
}
