package com.slate.service.calendar;

import com.slate.service.calendar.google.GoogleCalendarService;

import java.util.Calendar;

/**
 * Gets calendar details and events for a user.
 *
 * @see GoogleCalendarService
 */
public interface CalendarService {

  Calendar getPrimaryCalendar();

  String getCalendarEvents();
}
