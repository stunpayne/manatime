package com.slate.models.task;

import java.util.Date;

/**
 * A representation of a time slot. For any time slot, the required attributes are the start time
 * and the end time. Using these, we can arrive at the duration of the time slot.
 */
public class TimeSlot {

  //  Constant defining the number of milliseconds in a minute
  private static final long MILLIS_PER_MINUTE = 60 * 1000L;

  private Date startTime;
  private Date endTime;

  public static Builder builder() {
    return new Builder();
  }

  public Date getStartTime() {
    return startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public Long durationMinutes() {
    return (endTime.getTime() - startTime.getTime()) / MILLIS_PER_MINUTE;
  }

  public static final class Builder {

    private Date startTime;
    private Date endTime;

    private Builder() {}

    public Builder startTime(Date startTime) {
      this.startTime = startTime;
      return this;
    }

    public Builder endTime(Date endTime) {
      this.endTime = endTime;
      return this;
    }

    public TimeSlot build() {
      TimeSlot timeSlot = new TimeSlot();
      timeSlot.startTime = this.startTime;
      timeSlot.endTime = this.endTime;
      return timeSlot;
    }
  }
}
