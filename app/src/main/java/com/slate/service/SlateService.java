package com.slate.service;

import android.app.Activity;
import android.content.Intent;

import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.slate.activity.R;
import com.slate.models.calendar.Calendar;
import com.slate.models.calendar.CalendarEvent;
import com.slate.models.calendar.CalendarEventRequest;
import com.slate.service.calendar.CalendarService;
import com.slate.user.SignInHandler;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

public class SlateService {

  private static final String TAG = SlateService.class.getSimpleName();

  private final SignInHandler signInHandler;
  private final CalendarService calendarService;

  private GoogleSignInAccount account;

  @Inject
  public SlateService(SignInHandler signInHandler, CalendarService calendarService) {
    this.signInHandler = signInHandler;
    this.calendarService = calendarService;
  }

  /** Perform all activities needed to be done at the beginning of the app start. */
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
                  calendarService.getCalendarEvents(
                      getCalendarEventRequest(primaryCalendar, account.getEmail()));
              Log.d(TAG, "handleSignIn: " + calendarEvents);
              TextView eventsText = (TextView) activity.findViewById(R.id.event_text);
              eventsText.setText(calendarEvents.toString());
            });
  }

  private CalendarEventRequest getCalendarEventRequest(Calendar cal, String email) {
    return CalendarEventRequest.builder()
        .calendarId(cal.getId())
        .userId(email)
        .eventAfterTime(System.currentTimeMillis())
        .build();
  }
}
