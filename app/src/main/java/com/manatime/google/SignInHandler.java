package com.manatime.google;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.manatime.common.Constants;
import com.manatime.myapplication.GoogleCalendarFetcher;

import java.util.Optional;

import javax.inject.Inject;

public class SignInHandler {

  private final Activity activity;

  private static final String TAG = SignInHandler.class.getSimpleName();

  @Inject
  public SignInHandler(Application application) {
    Log.d("DAGGER", "ManatimeApplication hash in SignInHandler: " + application.hashCode());
    this.activity = null;
  }

  public View.OnClickListener createSignInButtonListener() {
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

    // Build a GoogleSignInClient with the options specified by gso.
    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this.activity, gso);

    return v -> {
      Intent signInIntent = googleSignInClient.getSignInIntent();
      this.activity.startActivityForResult(signInIntent, Constants.IntentRC.SIGN_IN);
    };
  }

  public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
      GoogleSignInAccount account = completedTask.getResult(ApiException.class);

      // Signed in successfully, show authenticated UI.
      Optional.ofNullable(account)
          .ifPresent(
              acc -> {
                String email = acc.getEmail();
                Log.d(TAG, "Got email: " + email);
                GoogleCalendarFetcher googleCalendarFetcher =
                    new GoogleCalendarFetcher(this.activity, this.activity, email);
                googleCalendarFetcher.fetchEvents();
              });
    } catch (ApiException e) {
      // The ApiException status code indicates the detailed failure reason.
      // Please refer to the GoogleSignInStatusCodes class reference for more information.
      Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
      Toast.makeText(this.activity, "Failed to fetch events!", Toast.LENGTH_SHORT).show();
    }
  }
}
