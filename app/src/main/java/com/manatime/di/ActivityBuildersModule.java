package com.manatime.di;

import com.manatime.myapplication.MainActivity;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

  @ContributesAndroidInjector
  abstract MainActivity contributeMainActivity();
}
