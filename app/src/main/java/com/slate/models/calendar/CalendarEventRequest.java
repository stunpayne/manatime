package com.slate.models.calendar;

public class CalendarEventRequest {
  private static final int DEFAULT_NUMBER_EVENTS = 10;

  String calendarId;
  String userId;
  Long eventAfterTime;
  int numberOfEvents;

  public CalendarEventRequest(String calendarId, String userId, Long eventAfterTime) {
    this(calendarId, userId, eventAfterTime, DEFAULT_NUMBER_EVENTS);
  }

  public CalendarEventRequest(
      String calendarId, String userId, Long eventAfterTime, int numberOfEvents) {
    this.calendarId = calendarId;
    this.userId = userId;
    this.eventAfterTime = eventAfterTime;
    this.numberOfEvents = numberOfEvents;
  }

  public static Builder builder() {
    return new Builder();
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

  public Long getEventAfterTime() {
    return eventAfterTime;
  }

  public void setEventAfterTime(Long eventAfterTime) {
    this.eventAfterTime = eventAfterTime;
  }

  public int getNumberOfEvents() {
    return numberOfEvents;
  }

  public void setNumberOfEvents(int numberOfEvents) {
    this.numberOfEvents = numberOfEvents;
  }

  public static final class Builder {

    String calendarId;
    String userId;
    Long eventAfterTime;
    int numberOfEvents;

    private Builder() {}

    public Builder calendarId(String calendarId) {
      this.calendarId = calendarId;
      return this;
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder eventAfterTime(Long eventAfterTime) {
      this.eventAfterTime = eventAfterTime;
      return this;
    }

    public Builder numberOfEvents(int numberOfEvents) {
      this.numberOfEvents = numberOfEvents;
      return this;
    }

    public CalendarEventRequest build() {
      CalendarEventRequest calendarEventRequest =
          new CalendarEventRequest(calendarId, userId, eventAfterTime);
      calendarEventRequest.setNumberOfEvents(numberOfEvents);
      return calendarEventRequest;
    }
  }

  @Override
  public String toString() {
    return "CalendarEventRequest{" +
        "calendarId='" + calendarId + '\'' +
        ", userId='" + userId + '\'' +
        ", eventAfterTime=" + eventAfterTime +
        ", numberOfEvents=" + numberOfEvents +
        '}';
  }
}
