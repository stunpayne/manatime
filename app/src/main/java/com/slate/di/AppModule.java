package com.slate.di;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import android.view.View;
import androidx.fragment.app.FragmentManager;
import com.slate.activity.MainActivity;
import com.slate.service.calendar.CalendarService;
import com.slate.service.calendar.google.GoogleCalendarService;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Callable;
import javax.inject.Named;

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

  @Named("SIGN_IN")
  @Provides
  static Callable<Void> signInCompleteCallback(MainActivity activity) {
    return activity.signInCompleteCallback();
  }

  @Named("CREATE_TASK")
  @Provides
  static View.OnClickListener createTaskButtonListener(MainActivity activity) {
    return activity.createTaskButtonListener();
  }
}
