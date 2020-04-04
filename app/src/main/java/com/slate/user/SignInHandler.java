package com.slate.user;

import android.app.Activity;
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
import com.slate.common.Constants;
import com.slate.service.calendar.GoogleCalendarFetcher;

import java.util.Optional;

import javax.inject.Inject;

public class SignInHandler {

  private static final String TAG = SignInHandler.class.getSimpleName();

  @Inject
  public SignInHandler() {}

  public View.OnClickListener createSignInButtonListener(Activity activity) {
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

    // Build a GoogleSignInClient with the options specified by gso.
    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, gso);

    return v -> {
      Intent signInIntent = googleSignInClient.getSignInIntent();
      activity.startActivityForResult(signInIntent, Constants.IntentRC.SIGN_IN);
    };
  }

  public GoogleSignInAccount handleSignInResult(
      Task<GoogleSignInAccount> completedTask, Activity activity) {
    try {
      GoogleSignInAccount account = completedTask.getResult(ApiException.class);

      // Signed in successfully, show authenticated UI.
      return Optional.ofNullable(account).orElse(null);
    } catch (ApiException e) {
      // The ApiException status code indicates the detailed failure reason.
      // Please refer to the GoogleSignInStatusCodes class reference for more information.
      Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
      Toast.makeText(activity, "Failed to fetch events!", Toast.LENGTH_SHORT).show();
      return null;
    }
  }
}
