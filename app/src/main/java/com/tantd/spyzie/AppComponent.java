package com.tantd.spyzie;

import com.tantd.spyzie.ui.schedule.ScheduleActivity;
import com.tantd.spyzie.ui.schedule.ScheduleFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(ScheduleActivity activity);

    void inject(ScheduleFragment fragment);
}
