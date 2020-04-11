package com.slate.common;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MutableCalendar extends GregorianCalendar {

  public MutableCalendar() {
    this(new Date());
  }

  public MutableCalendar(Date date) {
    setTime(date);
  }

  public MutableCalendar addDays(int days) {
    add(Calendar.DAY_OF_YEAR, days);
    return this;
  }

  /**
   * Sets the hour to the given parameter and all smaller time units to 0
   *
   * @param hour the hour to set
   * @return the MutableCalendar object
   */
  public MutableCalendar setExactHour(int hour) {
    setHour(hour);
    setMinute(0);
    setSecond(0);
    setMillis(0);
    return this;
  }

  public MutableCalendar setHour(int hour) {
    set(Calendar.HOUR_OF_DAY, hour);
    return this;
  }

  public MutableCalendar setMinute(int minute) {
    set(Calendar.MINUTE, minute);
    return this;
  }

  public MutableCalendar setSecond(int second) {
    set(Calendar.SECOND, second);
    return this;
  }

  public MutableCalendar setMillis(int millis) {
    set(Calendar.MILLISECOND, millis);
    return this;
  }
}
