package com.slate.models.slot;

/**
 * An enumeration of the different possible types of time slots
 *
 * @see Slot
 */
public enum SlotType {
  FREE,             //  No event scheduled during the slot
  BLOCKED_BY_USER,  //  Blocked by user without using this service
  OCCUPIED,         //  Blocked for an event by this service
  SCHEDULE          //  Meant to be used for scheduling a task
}
