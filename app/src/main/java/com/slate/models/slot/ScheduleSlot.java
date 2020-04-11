package com.slate.models.slot;

public class ScheduleSlot extends SimpleSlot {

  public ScheduleSlot(Long startTime, Long endTime) {
    super(startTime, endTime, SlotType.SCHEDULE);
  }
}
