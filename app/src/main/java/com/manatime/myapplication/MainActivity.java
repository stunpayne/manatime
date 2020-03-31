package com.manatime.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.manatime.common.Constants;
import com.manatime.google.SignInHandler;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private ManatimeService manatimeService;
  private SignInHandler signInHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    this.signInHandler = new SignInHandler(this);
    manatimeService = new ManatimeService(this, this.signInHandler);

    setupGoogleSignIn();
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
