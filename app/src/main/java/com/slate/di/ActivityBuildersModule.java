package com.slate.di;

import com.slate.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

  @ContributesAndroidInjector
  abstract MainActivity contributeMainActivity();
}
