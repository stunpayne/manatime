package com.slate.models.slot;

import com.slate.models.calendar.CalendarEvent;

public class CalendarEventSlot implements Slot {

  private final CalendarEvent calendarEvent;
  private final SlotType slotType;

  public CalendarEventSlot(CalendarEvent calendarEvent, SlotType slotType) {
    this.calendarEvent = calendarEvent;
    this.slotType = slotType;
  }

  @Override
  public Long getStartTime() {
    return calendarEvent.getStartTime();
  }

  @Override
  public Long getEndTime() {
    return calendarEvent.getEndTime();
  }

  @Override
  public void setEndTime(Long time) {

  }

  @Override
  public Long getDuration() {
    return calendarEvent.getDuration();
  }

  @Override
  public SlotType getType() {
    return slotType;
  }
}
