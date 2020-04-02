package com.manatime.di;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.manatime.google.SignInHandler;
import com.manatime.myapplication.MainActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

  @Provides
  static String test() {
    return "dagger test";
  }

  @Provides
  static boolean getApp(Application application)  {
    return application == null;
  }

  @Provides
  static Context provideContext(Application application)  {
    Log.d("DAGGER", "provideApplication: " + application.hashCode());
    return application.getApplicationContext();
  }

//  @Provides
//  static SignInHandler signInHandler(Application application)  {
//    Log.d("DAGGER", "application hash: " + application.hashCode());
//    return new SignInHandler(null, application);
//  }
}
