package com.slate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.slate.common.Constants;
import com.slate.service.SlateService;
import com.slate.user.SignInHandler;
import com.slate.user.UserPermission;

import java.util.Optional;
import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Inject SlateService slateService;

  @Inject SignInHandler signInHandler;

  @Inject
  public MainActivity() {}

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d("DAGGER", "MainActivity hash: " + hashCode());
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setupGoogleSignIn();
  }

  @Override
  protected void onStart() {
    super.onStart();

    //  Request calendar permissions from the user
    UserPermission.requestCalendarPermissions(this);

    Optional.ofNullable(slateService.checkForLogin(this))
        .ifPresent(
            account -> {
              //  Disable UI button
              //  Prepare current user's calendar
            });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
    if (requestCode == Constants.IntentRC.SIGN_IN) {
      slateService.handleSignIn(data, this);
    }
  }

  private void setupGoogleSignIn() {

    findViewById(R.id.sign_in_button)
        .setOnClickListener(signInHandler.createSignInButtonListener(this));
  }
}
