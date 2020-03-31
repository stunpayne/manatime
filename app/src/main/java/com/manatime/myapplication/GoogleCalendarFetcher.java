package com.manatime.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.widget.TextView;

import java.util.Date;
import java.util.Optional;

/** Fetches the events on the user's Google Calendar */
public class GoogleCalendarFetcher {

  private static final String TAG = GoogleCalendarFetcher.class.getSimpleName();

  private final Context context;
  private final Activity activity;
  private final String email;

  public GoogleCalendarFetcher(Context context, Activity activity, String email) {
    this.context = context;
    this.activity = activity;
    this.email = email;
  }

  // Projection array. Creating indices for this array instead of doing
  // dynamic lookups improves performance.
  private static final String[] CAL_PROJECTION =
      new String[] {
        Calendars._ID, // 0
        Calendars.ACCOUNT_NAME, // 1
        Calendars.CALENDAR_DISPLAY_NAME, // 2
        Calendars.OWNER_ACCOUNT, // 3
        Calendars.IS_PRIMARY // 4
      };

  // Projection array. Creating indices for this array instead of doing
  // dynamic lookups improves performance.
  private static final String[] EVENT_PROJECTION =
      new String[] {
        Events.CALENDAR_ID, // 0
        Events.ORGANIZER, // 1
        Events.TITLE, // 2
        Events.DTSTART, // 3
        Events.DTEND // 4
      };

  // The indices for the projection array above.
  private static final int PROJECTION_ID_INDEX = 0;
  private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
  private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
  private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
  private static final int PROJECTION_IS_PRIMARY_INDEX = 4;

  public void fetchEvents() {
    // Run query
    //    Cursor cur = null;
    ContentResolver cr = context.getContentResolver();
    String selection =
        "(("
            + Calendars.ACCOUNT_NAME
            + " = ?) AND ("
            + Calendars.ACCOUNT_TYPE
            + " = ?) AND ("
            + Calendars.OWNER_ACCOUNT
            + " = ?))";
    String[] selectionArgs = new String[] {email, "com.google", email};

    try (@SuppressLint("MissingPermission")
        Cursor cursor =
            cr.query(Calendars.CONTENT_URI, CAL_PROJECTION, selection, selectionArgs, null)) {

      Optional.ofNullable(cursor)
          .ifPresent(
              cur -> {
                Long primaryCalendarId = null;
                // Use the cursor to step through the returned records
                while (cur.moveToNext()) {

                  // Get the field values
                  long calID = cur.getLong(PROJECTION_ID_INDEX);
                  String displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                  String accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                  String ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
                  long isPrimary = cur.getLong(PROJECTION_IS_PRIMARY_INDEX);

                  Log.d(
                      TAG,
                      "calID: "
                          + calID
                          + " displayName: "
                          + displayName
                          + " isPrimary: "
                          + isPrimary);

                  if (isPrimary != 0) {
                    primaryCalendarId = calID;
                    break;
                  }
                }
                Optional.ofNullable(primaryCalendarId).ifPresent(id -> getCalendarEvents(cr, id));
              });
    }
  }

  private void getCalendarEvents(ContentResolver cr, Long calendarId) {
    String selection =
        "((" + Events.CALENDAR_ID + " = ?)" + " AND (" + Events.DTSTART + " > ?)" + ")";
    String[] selectionArgs =
        new String[] {calendarId.toString(), String.valueOf(System.currentTimeMillis())};
    try (@SuppressLint("MissingPermission")
        Cursor eventsCur =
            cr.query(Events.CONTENT_URI, EVENT_PROJECTION, selection, selectionArgs, null)) {
      printUpcomingEvents(eventsCur);
    }
  }

  private void printUpcomingEvents(Cursor eventsCur) {
    StringBuilder eventText = new StringBuilder("First 5 Events: \n");
    int count = 0;

    while (eventsCur.moveToNext() && count < 5) {
      long calId = eventsCur.getLong(0);
      String organizer = eventsCur.getString(1);
      String title = eventsCur.getString(2);
      Date startTime = new Date(eventsCur.getLong(3));
      Date endTime = new Date(eventsCur.getLong(4));

      String printText =
          "Title: " + title + "  Start: " + startTime.toString() + "  End: " + endTime.toString();
      eventText.append(printText).append("\n");
      count++;
    }

    Log.d(TAG, eventText.toString());
    TextView eventsText = this.activity.findViewById(R.id.event_text);
    eventsText.setText(eventText);
  }
}
