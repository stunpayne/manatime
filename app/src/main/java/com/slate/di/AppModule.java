package com.slate.di;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import android.view.View;
import com.slate.activity.MainActivity;
import com.slate.service.SharedPrefManager;
import com.slate.service.SimpleSharedPrefManager;
import com.slate.service.calendar.CalendarService;
import com.slate.service.calendar.google.GoogleCalendarService;
import com.slate.service.classifier.SimpleSlotter;
import com.slate.service.classifier.Slotter;
import com.slate.service.scheduler.FirstSlotTaskScheduler;
import com.slate.service.scheduler.TaskScheduler;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

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
  static CalendarService provideCalendarService(ContentResolver contentResolver) {
    return new GoogleCalendarService(contentResolver);
  }

  @Provides
  static TaskScheduler provideTaskScheduler() {
    return new FirstSlotTaskScheduler();
  }

  @Provides
  static Slotter provideSlotter() {
    return new SimpleSlotter();
  }

  @Provides
  @Singleton
  static SharedPrefManager provideSharedPrefManager(Context context) {
    return new SimpleSharedPrefManager(context);
  }

  @Named("CREATE_TASK")
  @Provides
  static View.OnClickListener createTaskButtonListener(MainActivity activity) {
    return activity.createTaskButtonListener();
  }
}
