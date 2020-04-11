package com.slate.service.classifier;

import com.google.api.client.util.Lists;
import com.slate.models.calendar.CalendarEvent;
import com.slate.models.slot.SimpleSlot;
import com.slate.models.slot.Slot;
import com.slate.models.slot.SlotType;
import java.util.Comparator;
import java.util.List;

class SimpleSlotter implements Slotter {

  @Override
  public List<Slot> createSlots(Long startTime, Long deadline, List<CalendarEvent> calendarEvents) {
    List<Slot> timeSlots = Lists.newArrayList();

    //  Sort events in ascending order of start time
    calendarEvents.sort(timeComparator);

    //  If the first event starts at the deadline moratorium, do not create a slot for that
    if (startTime < calendarEvents.get(0).getStartTime()) {
      timeSlots.add(createSlot(startTime, calendarEvents.get(0).getStartTime()));
    }

    //  For every event, create a blocked by user slot

    return null;
  }

  private Slot createSlot(Long start, Long end) {
    return new SimpleSlot(start, end, SlotType.FREE);
  }

  private boolean findLargestDuration(CalendarEvent first, CalendarEvent second) {
    //  Check if start time is the same
    return false;
  }

  private Comparator<CalendarEvent> timeComparator = (first, second) -> {
    return (int) (first.getStartTime() - second.getStartTime());
  };

  private boolean isEffectivelyEqualTime(Long time1, Long time2) {
    return Math.abs(time1 - time2) <= 5000;
  }
}
