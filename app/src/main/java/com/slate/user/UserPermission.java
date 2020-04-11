package com.slate.user;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;

public class UserPermission {

  private static final String TAG = UserPermission.class.getSimpleName();

  public static final void requestCalendarPermissions(Activity activity) {
    // Submit the query and get a Cursor object back.
    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR)
        != PackageManager.PERMISSION_GRANTED) {
      Log.w(TAG, "Calendar permission missing!");

      ActivityCompat.requestPermissions(
          activity, new String[]{Manifest.permission.READ_CALENDAR, permission.WRITE_CALENDAR}, 0);
    }
  }
}
