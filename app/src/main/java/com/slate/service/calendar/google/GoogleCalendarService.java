package com.slate.service.calendar.google;

import com.slate.service.calendar.CalendarService;

import java.util.Calendar;

import javax.inject.Inject;

/**
 *
 */
public class GoogleCalendarService implements CalendarService {

  @Inject
  public GoogleCalendarService() {}

  @Override
  public Calendar getPrimaryCalendar() {
    return null;
  }

  @Override
  public String getCalendarEvents() {
    return null;
  }
}
