package com.slate.di;

import com.slate.fragments.SignInFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

  @ContributesAndroidInjector
  abstract SignInFragment contributeSignInFragment();
}
