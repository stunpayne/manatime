package com.slate.models;

public class CalendarEventRequest {
  private static final int DEFAULT_NUMBER_EVENTS = 10;

  String calendarId;
  String userId;
  Long eventAfter;
  int numberOfEvents;

  public CalendarEventRequest(String calendarId, String userId, Long eventAfter) {
    this(calendarId, userId, eventAfter, DEFAULT_NUMBER_EVENTS);
  }

  public CalendarEventRequest(
      String calendarId, String userId, Long eventAfter, int numberOfEvents) {
    this.calendarId = calendarId;
    this.userId = userId;
    this.eventAfter = eventAfter;
    this.numberOfEvents = numberOfEvents;
  }

  public String getCalendarId() {
    return calendarId;
  }

  public void setCalendarId(String calendarId) {
    this.calendarId = calendarId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Long getEventAfter() {
    return eventAfter;
  }

  public void setEventAfter(Long eventAfter) {
    this.eventAfter = eventAfter;
  }

  public int getNumberOfEvents() {
    return numberOfEvents;
  }

  public void setNumberOfEvents(int numberOfEvents) {
    this.numberOfEvents = numberOfEvents;
  }


}
