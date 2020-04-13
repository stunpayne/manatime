package com.slate.models.slot;

/**
 * A base interface to any time slot
 */
public interface Slot {

  Long getStartTime();

  Long getEndTime();

  void setEndTime(Long time);

  Long getDuration();

  SlotType getType();
}
