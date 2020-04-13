package com.slate.models.slot;

public class SimpleSlot implements Slot {

  private final Long startTime;
  private Long endTime;
  private final SlotType slotType;

  public SimpleSlot(Long startTime, Long endTime, SlotType slotType) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.slotType = slotType;
  }

  @Override
  public Long getStartTime() {
    return startTime;
  }

  @Override
  public Long getEndTime() {
    return endTime;
  }

  @Override
  public void setEndTime(Long time) {
    this.endTime = time;
  }

  @Override
  public Long getDuration() {
    return getEndTime() - getStartTime();
  }

  @Override
  public SlotType getType() {
    return slotType;
  }
}
