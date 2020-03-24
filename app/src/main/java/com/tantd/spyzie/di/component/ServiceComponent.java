package com.tantd.spyzie.di.component;

import com.tantd.spyzie.di.ServiceScope;
import com.tantd.spyzie.di.module.ServiceModule;

import dagger.Subcomponent;

@Subcomponent(modules = {ServiceModule.class})
@ServiceScope
public interface ServiceComponent {
}
