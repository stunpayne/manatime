package com.slate.di;

import com.slate.fragments.CalendarFragment;
import com.slate.fragments.CheckableTaskFragment;
import com.slate.fragments.CreateTaskFragment;
import com.slate.fragments.HomeScreenFragment;
import com.slate.fragments.RewardsFragment;
import com.slate.fragments.SignInFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

  @ContributesAndroidInjector
  abstract SignInFragment contributeSignInFragment();

  @ContributesAndroidInjector
  abstract CreateTaskFragment contributeCreateTaskFragment();

  @ContributesAndroidInjector
  abstract HomeScreenFragment contributeHomeScreenFragment();

  @ContributesAndroidInjector
  abstract CalendarFragment contributeCalendarFragment();

  @ContributesAndroidInjector
  abstract RewardsFragment contributeRewardsFragment();

  @ContributesAndroidInjector
  abstract CheckableTaskFragment contributeCheckableTaskFragment();
}
