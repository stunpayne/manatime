package com.slate.service;

import android.app.Activity;
import android.content.Intent;

import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.slate.activity.MainActivity;
import com.slate.activity.R;
import com.slate.models.calendar.Calendar;
import com.slate.models.calendar.CalendarEvent;
import com.slate.models.calendar.CalendarEventRequest;
import com.slate.service.calendar.CalendarService;
import com.slate.user.SignInHandler;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import javax.inject.Named;

public class SlateService {

  private static final String TAG = SlateService.class.getSimpleName();
  private static final Long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L;

  private final SignInHandler signInHandler;
  private final CalendarService calendarService;
  private final Callable<Void> signInCompleteCallback;

  private GoogleSignInAccount account;

  @Inject
  public SlateService(SignInHandler signInHandler, CalendarService calendarService,
      @Named("SIGN_IN") Callable<Void> signInCompleteCallback) {
    this.signInHandler = signInHandler;
    this.calendarService = calendarService;
    this.signInCompleteCallback = signInCompleteCallback;
  }

  /**
   * Perform all activities needed to be done at the beginning of the app start.
   */
  public final GoogleSignInAccount checkForLogin(Activity activity) {
    // Check for existing Google Sign In account, if the user is already signed in
    // the GoogleSignInAccount will be non-null.
    this.account = GoogleSignIn.getLastSignedInAccount(activity);
    return this.account;
  }

  public final void handleSignIn(Intent intent, Activity activity) {
    // The Task returned from this call is always completed, no need to attach a listener.
    Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(intent);

    // If the account is present, get the next calendar events
    Optional.ofNullable(signInHandler.handleSignInResult(signInTask, activity))
        .ifPresent(
            account -> {
              Calendar primaryCalendar = calendarService.getPrimaryCalendar(account.getEmail());
              Log.d(TAG, "calendar: " + primaryCalendar);

              List<CalendarEvent> calendarEvents =
                  calendarService
                      .getCalendarEvents(getCalendarEventRequest(primaryCalendar.getId()));
              Log.d(TAG, "handleSignIn: " + calendarEvents);

              TextView eventsText = activity.findViewById(R.id.event_text);
              eventsText.setText(calendarEvents.toString());
            });

    try {
      this.signInCompleteCallback.call();
    } catch (Exception e) {
      Log.d(TAG, "Error occurred in the sign in complete callback ", e);
    }
  }

  private CalendarEventRequest getCalendarEventRequest(String calId) {
    long now = System.currentTimeMillis();
    return CalendarEventRequest.builder()
        .calendarId(calId)
        .endTimeAfter(now)
        .startTimeBefore(now + 5 * ONE_DAY_MILLIS)
        .build();
  }
}
