package com.manatime.myapplication;

import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.manatime.common.Constants;
import com.manatime.google.SignInHandler;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private ManatimeService manatimeService;

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

    manatimeService = new ManatimeService(this, this.signInHandler);

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

    manatimeService.startService();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
    if (requestCode == Constants.IntentRC.SIGN_IN) {
      manatimeService.handleSignIn(data);
    }
  }

  private void setupGoogleSignIn() {

    findViewById(R.id.sign_in_button)
        .setOnClickListener(signInHandler.createSignInButtonListener());
  }
}
