package com.tantd.spyzie.di.component;

import com.tantd.spyzie.data.device.worker.GetCallsWorker;
import com.tantd.spyzie.data.device.worker.GetContactsWorker;
import com.tantd.spyzie.data.device.worker.RefreshTokenWorker;
import com.tantd.spyzie.di.ServiceScope;
import com.tantd.spyzie.di.module.ServiceModule;
import com.tantd.spyzie.service.MainService;

import dagger.Subcomponent;

@Subcomponent(modules = {ServiceModule.class})
@ServiceScope
public interface ServiceComponent {

    void inject(MainService service);

    void inject(GetContactsWorker worker);

    void inject(GetCallsWorker worker);

    void inject(RefreshTokenWorker worker);
}
