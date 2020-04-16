package com.slate.service;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import javax.inject.Inject;

public class SimpleSharedPrefManager implements SharedPrefManager {

  private final Context context;

  @Inject
  public SimpleSharedPrefManager(Context context) {
    this.context = context;
  }

  private static class Keys {

    static String USER_INFO = "user.info";
    static String USER_EMAIL = "user.email";
  }

  @Override
  public String getSignedInUserEmail() {
    return context.getSharedPreferences(Keys.USER_INFO, Context.MODE_PRIVATE)
        .getString(Keys.USER_EMAIL, null);
  }

  @Override
  public void setSignedInUserEmail(String email) {
    Editor editor = context.getSharedPreferences(Keys.USER_INFO, Context.MODE_PRIVATE).edit();
    editor.putString(Keys.USER_EMAIL, email).apply();
  }
}
