package com.slate.service.calendar.google;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import com.google.common.collect.Lists;
import com.slate.models.calendar.Calendar;
import com.slate.models.calendar.CalendarEvent;
import com.slate.models.calendar.CalendarEventRequest;
import com.slate.service.calendar.CalendarService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

/** */
public class GoogleCalendarService implements CalendarService {

  private static final String TAG = GoogleCalendarService.class.getSimpleName();

  // Projection array. Creating indices for this array instead of doing
  // dynamic lookups improves performance.
  private static final String[] CAL_PROJECTION =
      new String[] {
        Calendars._ID, // 0
        Calendars.CALENDAR_DISPLAY_NAME, // 1
        Calendars.ACCOUNT_NAME, // 2
        Calendars.OWNER_ACCOUNT, // 3
        Calendars.IS_PRIMARY, // 4
        Calendars.VISIBLE, // 5
        Calendars.CALENDAR_TIME_ZONE // 6
      };

  // Projection array. Creating indices for this array instead of doing
  // dynamic lookups improves performance.
  private static final String[] EVENT_PROJECTION =
      new String[] {
        Events.CALENDAR_ID, // 0
        Events.ORGANIZER, // 1
        Events.TITLE, // 2
        Events.DTSTART, // 3
        Events.DTEND, // 4
        Events._ID, // 5
        Events.STATUS, // 6
        Events.EVENT_LOCATION, // 7
        Events.DESCRIPTION, // 8
        Events.EVENT_TIMEZONE // 9
      };

  // The indices for calendar projection array above.
  private static final int CAL_PROJECTION_ID_INDEX = 0;
  private static final int CAL_PROJECTION_DISPLAY_NAME_INDEX = 1;
  private static final int CAL_PROJECTION_ACCOUNT_NAME_INDEX = 2;
  private static final int CAL_PROJECTION_OWNER_ACCOUNT_INDEX = 3;
  private static final int CAL_PROJECTION_IS_PRIMARY_INDEX = 4;
  private static final int CAL_PROJECTION_IS_VISIBLE_INDEX = 5;
  private static final int CAL_PROJECTION_TIMEZONE_INDEX = 6;

  // The indices for calendar projection array above.
  private static final int EVENT_PROJECTION_CAL_ID_INDEX = 0;
  private static final int EVENT_PROJECTION_ORGANIZER_INDEX = 1;
  private static final int EVENT_PROJECTION_TITLE_INDEX = 2;
  private static final int EVENT_PROJECTION_DTSTART_INDEX = 3;
  private static final int EVENT_PROJECTION_DTEND_INDEX = 4;
  private static final int EVENT_PROJECTION_EVENT_ID_INDEX = 5;
  private static final int EVENT_PROJECTION_STATUS_INDEX = 6;
  private static final int EVENT_PROJECTION_LOCATION_INDEX = 7;
  private static final int EVENT_PROJECTION_DESC_INDEX = 8;
  private static final int EVENT_PROJECTION_TIMEZONE_INDEX = 9;

  private final ContentResolver contentResolver;

  @Inject
  public GoogleCalendarService(ContentResolver contentResolver) {
    this.contentResolver = contentResolver;
  }

  @Override
  public Calendar getPrimaryCalendar(String accountName) {

    //  Values for the "where part of query
    String[] selectionArgs = new String[] {accountName, "com.google", accountName};

    try (@SuppressLint("MissingPermission")
        Cursor cursor =
            contentResolver.query(
                Calendars.CONTENT_URI, CAL_PROJECTION, getCalSelection(), selectionArgs, null)) {

      return Optional.ofNullable(cursor)
          .map(
              cur -> {
                // Use the cursor to step through the returned records
                if (cur.moveToNext()) {
                  // Get the field values
                  long calID = cur.getLong(CAL_PROJECTION_ID_INDEX);
                  String displayName = cur.getString(CAL_PROJECTION_DISPLAY_NAME_INDEX);
                  String accName = cur.getString(CAL_PROJECTION_ACCOUNT_NAME_INDEX);
                  String ownerName = cur.getString(CAL_PROJECTION_OWNER_ACCOUNT_INDEX);
                  long isPrimary = cur.getLong(CAL_PROJECTION_IS_PRIMARY_INDEX);
                  long visible = cur.getLong(CAL_PROJECTION_IS_VISIBLE_INDEX);
                  String timeZone = cur.getString(CAL_PROJECTION_TIMEZONE_INDEX);

                  Calendar calendar =
                      createCalendar(
                          String.valueOf(calID),
                          displayName,
                          accName,
                          ownerName,
                          isPrimary == 1,
                          timeZone,
                          visible == 1);

                  return calendar;
                }
                return null;
              })
          .orElse(null);
    }
  }

  @Override
  public List<CalendarEvent> getCalendarEvents(CalendarEventRequest request) {
    String selection =
        "((" + Events.CALENDAR_ID + " = ?)" + " AND (" + Events.DTSTART + " > ?)" + ")";
    String[] selectionArgs =
        new String[] {request.getCalendarId(), String.valueOf(request.getEventAfterTime())};

    try (@SuppressLint("MissingPermission")
        Cursor eventsCur =
            contentResolver.query(
                Events.CONTENT_URI, EVENT_PROJECTION, selection, selectionArgs, null)) {
      return Optional.ofNullable(eventsCur)
          .map(this::getUpcomingEvents)
          .orElse(Lists.newArrayList());
    }
  }

  /**
   * Provides fields for the where part of the query
   *
   * @return fields in the where part of the query
   */
  private String getCalSelection() {
    return "(("
        + Calendars.ACCOUNT_NAME
        + " = ?) AND ("
        + Calendars.ACCOUNT_TYPE
        + " = ?) AND ("
        + Calendars.OWNER_ACCOUNT
        + " = ?))";
  }

  private Calendar createCalendar(
      String id,
      String displayName,
      String accountName,
      String ownerAccount,
      boolean primary,
      String timeZone,
      boolean visible) {

    return Calendar.builder()
        .id(id)
        .displayName(displayName)
        .accountName(accountName)
        .ownerAccount(ownerAccount)
        .primary(primary)
        .timeZone(timeZone)
        .visible(visible)
        .build();
  }

  private List<CalendarEvent> getUpcomingEvents(Cursor eventsCur) {
    StringBuilder eventText = new StringBuilder("First 5 Events: \n");
    int count = 0;

    List<CalendarEvent> calendarEvents = Lists.newArrayList();

    while (eventsCur.moveToNext() && count < 5) {
      long calId = eventsCur.getLong(EVENT_PROJECTION_CAL_ID_INDEX);
      String organizer = eventsCur.getString(EVENT_PROJECTION_ORGANIZER_INDEX);
      String title = eventsCur.getString(EVENT_PROJECTION_TITLE_INDEX);
      Date startTime = new Date(eventsCur.getLong(EVENT_PROJECTION_DTSTART_INDEX));
      Date endTime = new Date(eventsCur.getLong(EVENT_PROJECTION_DTEND_INDEX));
      String eventId = eventsCur.getString(EVENT_PROJECTION_EVENT_ID_INDEX);
      String status = eventsCur.getString(EVENT_PROJECTION_STATUS_INDEX);
      String location = eventsCur.getString(EVENT_PROJECTION_LOCATION_INDEX);
      String description = eventsCur.getString(EVENT_PROJECTION_DESC_INDEX);
      String timeZone = eventsCur.getString(EVENT_PROJECTION_TIMEZONE_INDEX);

      String printText =
          "Title: " + title + "  Start: " + startTime.toString() + "  End: " + endTime.toString();
      eventText.append(printText).append("\n");

      calendarEvents.add(
          getCalEvent(
              eventId,
              String.valueOf(calId),
              title,
              description,
              organizer,
              location,
              timeZone,
              status,
              startTime.getTime(),
              endTime.getTime()));

      count++;
    }

    return calendarEvents;
  }

  private CalendarEvent getCalEvent(
      String id,
      String calendarId,
      String title,
      String description,
      String organizer,
      String location,
      String timeZone,
      String status,
      Long startTime,
      Long endTime) {
    return CalendarEvent.builder()
        .id(id)
        .calendarId(calendarId)
        .title(title)
        .description(description)
        .organizer(organizer)
        .location(location)
        .status(status)
        .timeZone(timeZone)
        .startTime(startTime)
        .endTime(endTime)
        .build();
  }
}
