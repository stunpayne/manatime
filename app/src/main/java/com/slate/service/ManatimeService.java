package com.slate.service;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.slate.service.calendar.google.GoogleCalendarService;
import com.slate.user.SignInHandler;

import javax.inject.Inject;

public class ManatimeService {

  private final SignInHandler signInHandler;
  private final GoogleCalendarService eventService;

  @Inject
  public ManatimeService(SignInHandler signInHandler) {
    this.signInHandler = signInHandler;
    this.eventService = new GoogleCalendarService();
  }

  /** Perform all activities needed to be done at the beginning of the app start. */
  public final void startService(Activity activity) {
    // Check for existing Google Sign In account, if the user is already signed in
    // the GoogleSignInAccount will be non-null.
    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
  }

  public final void handleSignIn(Intent intent, Activity activity) {
    // The Task returned from this call is always completed, no need to attach
    // a listener.
    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
    signInHandler.handleSignInResult(task, activity);
  }
}
