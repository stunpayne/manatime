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
import com.slate.models.user.GoogleUser;
import com.slate.models.user.SignedInUser;
import com.slate.service.calendar.CalendarService;
import com.slate.user.SignInHandler;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

public class SlateService {

  private static final String TAG = SlateService.class.getSimpleName();
  private static final Long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L;

  private final SignInHandler signInHandler;
  private final CalendarService calendarService;
  private final SharedPrefManager sharedPrefManager;

  private GoogleSignInAccount account;

  @Inject
  public SlateService(SignInHandler signInHandler, CalendarService calendarService,
      SharedPrefManager sharedPrefManager) {
    this.signInHandler = signInHandler;
    this.calendarService = calendarService;
    this.sharedPrefManager = sharedPrefManager;
  }

  /**
   * Perform all activities needed to be done at the beginning of the app start.
   */
  public final SignedInUser checkForLogin(Activity activity) {
    // Check for existing Google Sign In account, if the user is already signed in
    // the GoogleSignInAccount will be non-null.
    this.account = GoogleSignIn.getLastSignedInAccount(activity);
    return Optional.ofNullable(account)
        .map(acc -> GoogleUser.builder().account(acc).build())
        .orElse(null);
  }

  public final SignedInUser handleSignIn(Intent intent, Activity activity) {
    // The Task returned from this call is always completed, no need to attach a listener.
    Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(intent);

    // If the account is present, get the next calendar events
    SignedInUser signedInAccount = signInHandler.getSignedInAccount(signInTask, activity);
    Optional.ofNullable(signedInAccount)
        .ifPresent(
            account -> {
              //  Persist the info in SharedPreferences
              sharedPrefManager.setSignedInUserEmail(account.getEmail());
              getCalendarAndEvents(activity, account.getEmail());
            });
    return signedInAccount;
  }

  private void getCalendarAndEvents(Activity activity, String email) {
    Calendar primaryCalendar = calendarService.getPrimaryCalendar(email);
    Log.d(TAG, "calendar: " + primaryCalendar);

    List<CalendarEvent> calendarEvents =
        calendarService
            .getCalendarEvents(getCalendarEventRequest(primaryCalendar.getId()));
    Log.d(TAG, "handleSignIn: " + calendarEvents);

    StringBuilder builder = new StringBuilder("Events on Calendar\n");
    calendarEvents.forEach(event -> builder.append(event.getTitle()).append("\n"));
    TextView eventsText = activity.findViewById(R.id.event_text);
    eventsText.setText(builder.toString());
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
