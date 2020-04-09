package com.slate.di;

import com.slate.activity.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

  @ContributesAndroidInjector(
      modules = {MainFragmentBuildersModule.class}
  )
  abstract MainActivity contributeMainActivity();
}
