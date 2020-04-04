package com.slate.di;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import com.slate.service.calendar.CalendarService;
import com.slate.service.calendar.google.GoogleCalendarService;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

  @Provides
  static String test() {
    return "dagger test";
  }

  @Provides
  static boolean getApp(Application application) {
    return application == null;
  }

  @Provides
  static Context provideContext(Application application) {
    Log.d("DAGGER", "provideApplication: " + application.hashCode());
    return application.getApplicationContext();
  }

  @Provides
  static ContentResolver provideContentResolver(Application application) {
    return application.getContentResolver();
  }

  @Provides
  static CalendarService provideCalendarService(
      Application application, ContentResolver contentResolver) {
    return new GoogleCalendarService(contentResolver);
  }
}