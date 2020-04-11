package com.slate.models.slot;

/**
 * A base interface to any time slot
 */
public interface Slot {

  Long getStartTime();

  Long getEndTime();

  Long getDuration();

  SlotType getType();
}
