package com.slate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.slate.common.Constants;
import com.slate.service.ManatimeService;
import com.slate.user.SignInHandler;
import com.slate.user.UserPermission;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Inject
  ManatimeService manatimeService;

  @Inject
  SignInHandler signInHandler;

  @Inject
  String test;

  @Inject
  Boolean getApp;

  @Inject
  Context appContext;

  @Inject
  public MainActivity() {
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d("DAGGER", "MainActivity hash: " + hashCode());
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    manatimeService = new ManatimeService(this.signInHandler);

    setupGoogleSignIn();

    Log.d("DAGGER", "onCreate string: " + test);
    Log.d("DAGGER", "onCreate boolean: " + getApp);
    Log.d("DAGGER", "onCreate appContext: " + appContext.hashCode());
  }

  @Override
  protected void onStart() {
    super.onStart();

    //  Request calendar permissions from the user
    UserPermission.requestCalendarPermissions(this);

    manatimeService.startService(this);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
    if (requestCode == Constants.IntentRC.SIGN_IN) {
      manatimeService.handleSignIn(data, this);
    }
  }

  private void setupGoogleSignIn() {

    findViewById(R.id.sign_in_button)
        .setOnClickListener(signInHandler.createSignInButtonListener(this));
  }
}
