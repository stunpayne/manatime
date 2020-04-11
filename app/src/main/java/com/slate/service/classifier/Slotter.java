package com.slate.service.classifier;

import com.slate.models.calendar.CalendarEvent;
import com.slate.models.slot.Slot;
import java.util.List;

/**
 * Responsible for dividing big time intervals into slots based on events scheduled during the
 * intervals
 */
public interface Slotter {

  /**
   * Given a starting time, a deadline and a list of events between them, creates a list of typed
   * time slots
   *
   * @return a list of typed slots
   */
  List<Slot> createSlots(Long startTime, Long deadline, List<CalendarEvent> calendarEvents);
}
