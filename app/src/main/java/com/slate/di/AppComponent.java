package com.slate.di;

import android.app.Application;
import com.slate.SlateApplication;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Singleton
@Component(
    modules = {AndroidSupportInjectionModule.class, ActivityBuildersModule.class, AppModule.class})
public interface AppComponent extends AndroidInjector<SlateApplication> {

  @Component.Builder
  interface Builder {

    @BindsInstance
    Builder application(Application application);

    AppComponent build();
  }
}
