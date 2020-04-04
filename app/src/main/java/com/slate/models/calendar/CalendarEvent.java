package com.slate.models.calendar;

import java.util.Date;

public class CalendarEvent {
  private String id;
  private String calendarId;

  private String title;
  private String description;
  private String organizer;
  private String location;
  private String timeZone;
  private String status;

  private Long startTime;
  private Long endTime;

  public static Builder builder()  {
    return new Builder();
  }

  public String getId() {
    return id;
  }

  public String getCalendarId() {
    return calendarId;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getOrganizer() {
    return organizer;
  }

  public String getLocation() {
    return location;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public String getStatus() {
    return status;
  }

  public Long getStartTime() {
    return startTime;
  }

  public Long getEndTime() {
    return endTime;
  }


  public static final class Builder {

    private String id;
    private String calendarId;
    private String title;
    private String description;
    private String organizer;
    private String location;
    private String timeZone;
    private String status;
    private Long startTime;
    private Long endTime;

    private Builder() {
    }

    public static Builder aCalendarEvent() {
      return new Builder();
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder calendarId(String calendarId) {
      this.calendarId = calendarId;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder organizer(String organizer) {
      this.organizer = organizer;
      return this;
    }

    public Builder location(String location) {
      this.location = location;
      return this;
    }

    public Builder timeZone(String timeZone) {
      this.timeZone = timeZone;
      return this;
    }

    public Builder status(String status) {
      this.status = status;
      return this;
    }

    public Builder startTime(Long startTime) {
      this.startTime = startTime;
      return this;
    }

    public Builder endTime(Long endTime) {
      this.endTime = endTime;
      return this;
    }

    public CalendarEvent build() {
      CalendarEvent calendarEvent = new CalendarEvent();
      calendarEvent.title = this.title;
      calendarEvent.id = this.id;
      calendarEvent.calendarId = this.calendarId;
      calendarEvent.organizer = this.organizer;
      calendarEvent.status = this.status;
      calendarEvent.endTime = this.endTime;
      calendarEvent.timeZone = this.timeZone;
      calendarEvent.description = this.description;
      calendarEvent.startTime = this.startTime;
      calendarEvent.location = this.location;
      return calendarEvent;
    }
  }

  @Override
  public String toString() {
    return "CalendarEvent{" +
        "id='" + id + '\'' +
        ", calendarId='" + calendarId + '\'' +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", organizer='" + organizer + '\'' +
        ", location='" + location + '\'' +
        ", timeZone='" + timeZone + '\'' +
        ", status='" + status + '\'' +
        ", startTime=" + new Date(startTime).toString() +
        ", endTime=" + new Date(endTime).toString() +
        '}';
  }
}
