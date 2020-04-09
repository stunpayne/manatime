package com.slate.service.calendar;

import com.slate.models.calendar.Calendar;
import com.slate.models.calendar.CalendarEvent;
import com.slate.models.calendar.CalendarEventRequest;
import com.slate.service.calendar.google.GoogleCalendarService;
import java.util.List;

/**
 * Gets calendar details and events for a user.
 *
 * @see GoogleCalendarService
 */
public interface CalendarService {

  //  TODO: Create separate models for calendar and event. Right now, we're not using any calendar
  // other than Google so it's not an urgent requirement.
  /**
   * Gets the primary calendar of the user. Account name can be anything (user ID, email ID etc.)
   * depending on the account. For our use case, it's the user's email.
   *
   * @param accountName the email ID of the user
   * @return a Calendar object containing required info of the user's calendar
   */
  Calendar getPrimaryCalendar(String accountName);

  /**
   * Gets events from a user's calendar given a request object.
   *
   * @param request the CalendarEventRequest
   * @return a list of events from the user's calendar
   */
  List<CalendarEvent> getCalendarEvents(CalendarEventRequest request);
}
