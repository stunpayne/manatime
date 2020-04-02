package com.manatime;

import android.util.Log;

import com.manatime.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class ManatimeApplication extends DaggerApplication {

  @Override
  protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
    Log.d("DAGGER", "ManatimeApplication hash: " + hashCode());
    return DaggerAppComponent.builder().application(this).build();
  }
}
