package com.slate.models.calendar;

public class CalendarEventRequest {

  private static final int DEFAULT_NUMBER_EVENTS = 10;

  private String calendarId;
  private Long endTimeAfter;
  private Long startTimeBefore;
  private int numberOfEvents;

  private CalendarEventRequest(String calendarId, Long endTimeAfter, Long startTimeBefore) {
    this(calendarId, endTimeAfter, startTimeBefore, DEFAULT_NUMBER_EVENTS);
  }

  private CalendarEventRequest(String calendarId, Long endTimeAfter, Long startTimeBefore,
      int numberOfEvents) {
    this.calendarId = calendarId;
    this.endTimeAfter = endTimeAfter;
    this.startTimeBefore = startTimeBefore;
    this.numberOfEvents = numberOfEvents;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getCalendarId() {
    return calendarId;
  }

  public Long getEndTimeAfter() {
    return endTimeAfter;
  }

  public Long getStartTimeBefore() {
    return startTimeBefore;
  }

  public int getNumberOfEvents() {
    return numberOfEvents;
  }

  public static final class Builder {

    private String calendarId;
    private Long endTimeAfter;
    private Long startTimeBefore;
    private int numberOfEvents = Integer.MAX_VALUE;

    private Builder() {
    }

    public Builder calendarId(String calendarId) {
      this.calendarId = calendarId;
      return this;
    }

    public Builder endTimeAfter(Long endTimeAfter) {
      this.endTimeAfter = endTimeAfter;
      return this;
    }

    public Builder startTimeBefore(Long startTimeBefore) {
      this.startTimeBefore = startTimeBefore;
      return this;
    }

    public Builder numberOfEvents(int numberOfEvents) {
      this.numberOfEvents = numberOfEvents;
      return this;
    }

    public CalendarEventRequest build() {
      CalendarEventRequest calendarEventRequest =
          new CalendarEventRequest(calendarId, endTimeAfter, startTimeBefore, numberOfEvents);
      return calendarEventRequest;
    }
  }

  @Override
  public String toString() {
    return "CalendarEventRequest{" +
        "calendarId='" + calendarId + '\'' +
        ", endTimeAfter=" + endTimeAfter +
        ", numberOfEvents=" + numberOfEvents +
        '}';
  }
}
