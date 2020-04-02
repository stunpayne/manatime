package com.manatime.di;

import android.app.Application;

import com.manatime.ManatimeApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
    modules = {AndroidSupportInjectionModule.class, ActivityBuildersModule.class, AppModule.class})
public interface AppComponent extends AndroidInjector<ManatimeApplication> {

  @Component.Builder
  interface Builder {

    @BindsInstance
    Builder application(Application application);

    AppComponent build();
  }
}
