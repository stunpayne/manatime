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

  public MutableCalendar addDays(int days)  {
    add(Calendar.DAY_OF_YEAR, days);
    return this;
  }

  public MutableCalendar setHour(int hour) {
    set(Calendar.HOUR, hour);
    return this;
  }
}
