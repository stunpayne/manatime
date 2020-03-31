package com.manatime.myapplication;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.manatime.google.AccountService;
import com.manatime.google.CalendarEventService;
import com.manatime.google.SignInHandler;

public class ManatimeService {
  private final Activity activity;

  private final SignInHandler signInHandler;
  private final AccountService accountService;
  private final CalendarEventService eventService;

  public ManatimeService(Activity activity, SignInHandler signInHandler) {
    this.activity = activity;
    this.signInHandler = signInHandler;
    this.accountService = new AccountService();
    this.eventService = new CalendarEventService();
  }

  /** Perform all activities needed to be done at the beginning of the app start. */
  public final void startService() {
    // Check for existing Google Sign In account, if the user is already signed in
    // the GoogleSignInAccount will be non-null.
    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.activity);
  }

  public final void handleSignIn(Intent intent) {
    // The Task returned from this call is always completed, no need to attach
    // a listener.
    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
    signInHandler.handleSignInResult(task);
  }
}
