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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.slate.activity.MainActivity;
import com.slate.common.Constants;
import com.slate.models.user.GoogleUser;
import com.slate.models.user.SignedInUser;
import java.util.Optional;
import javax.inject.Inject;

public class SignInHandler {

  private static final String TAG = SignInHandler.class.getSimpleName();

  private final GoogleSignInOptions googleSignInOptions;

  @Inject
  public SignInHandler() {
    this.googleSignInOptions = new Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
        .build();
  }

  /**
   * Returns an on click listener for the Sign-In button on the main page
   *
   * @return on click listener for the sign in button
   */
  public View.OnClickListener createSignInButtonListener(Activity activity) {
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInClient client = GoogleSignIn.getClient(activity, googleSignInOptions);
    return v -> {
      Intent signInIntent = client.getSignInIntent();
      activity.startActivityForResult(signInIntent, Constants.IntentRC.SIGN_IN);
    };
  }

  /**
   * Returns details of the user once they've signed in
   *
   * @param completedTask the result of the Sign In intent
   * @param activity      the Activity containing the Sign-in button
   * @return
   */
  public SignedInUser getSignedInAccount(Task<GoogleSignInAccount> completedTask,
      Activity activity) {
    try {
      GoogleSignInAccount account = completedTask.getResult(ApiException.class);

      // Signed in successfully, show authenticated UI.
      return Optional.ofNullable(account)
          .map(acc -> GoogleUser.builder().account(acc).build())
          .orElse(null);
    } catch (ApiException e) {
      // The ApiException status code indicates the detailed failure reason.
      // Please refer to the GoogleSignInStatusCodes class reference for more information.
      Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
      Toast.makeText(activity, "Failed to fetch events!", Toast.LENGTH_SHORT).show();
      return null;
    }
  }

  /**
   * Signs the user out of Google
   *
   * @param activity the Activity used to create the google sign in client
   */
  public void handleSignOut(Activity activity) {
    GoogleSignIn.getClient(activity, googleSignInOptions).signOut();
  }
}
