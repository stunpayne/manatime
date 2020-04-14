package com.slate.service.classifier;

import com.google.common.collect.Lists;
import com.slate.models.calendar.CalendarEvent;
import com.slate.models.slot.CalendarEventSlot;
import com.slate.models.slot.SimpleSlot;
import com.slate.models.slot.Slot;
import com.slate.models.slot.SlotType;
import java.util.Comparator;
import java.util.List;

public class SimpleSlotter implements Slotter {

  private static final String TAG = SimpleSlotter.class.getSimpleName();

  private static final Comparator<CalendarEvent> timeComparator = (first, second) -> {
    if (first.getStartTime() < second.getStartTime()) {
      return -1;
    }

    if (first.getStartTime().equals(second.getStartTime())) {
      return first.getEndTime().compareTo(second.getEndTime());
    }

    return 1;
  };

  void sortEvents(List<CalendarEvent> events) {
    events.sort(timeComparator);
  }

  @Override
  public List<Slot> createSlots(Long startTime, Long deadline, List<CalendarEvent> calendarEvents) {

    //  If there are no events, the entire time duration is a free slot
    if (calendarEvents.size() == 0) {
      return Lists.newArrayList(new SimpleSlot(startTime, deadline, SlotType.FREE));
    }
//    else if (calendarEvents.size() == 1) {
//      //  TODO: Check if this event is scheduled by us, then mark as OCCUPIED
//      SlotType slotType = SlotType.BLOCKED_BY_USER;
//      return Lists.newArrayList(new CalendarEventSlot(calendarEvents.get(0), slotType));
//    }

    List<Slot> timeSlots = Lists.newArrayList();

    //  Sort events in ascending order of start time
    calendarEvents.sort(timeComparator);

    //  If the start time is before the time the first event starts at, there is a free slot
    if (startTime < calendarEvents.get(0).getStartTime()) {
      timeSlots.add(new SimpleSlot(startTime, calendarEvents.get(0).getStartTime(), SlotType.FREE));
    }

    SlottingResult slottingResult = createAllSlots(calendarEvents);
    timeSlots.addAll(slottingResult.getSlots());

    //  If the last event end at the deadline moratorium, do not create a slot for that
    if (slottingResult.getLastEvent().getEndTime() < deadline) {
      timeSlots.add(
          new SimpleSlot(slottingResult.getLastEvent().getEndTime(), deadline, SlotType.FREE));
    }

    return timeSlots;
  }

  /**
   * Given a list of the sorted calendar events, returns a list of blocked, occupied and free slots
   * between the events
   *
   * @param calendarEvents all the events currently scheduled on the calendar
   * @return a list of classified slots
   */
  SlottingResult createAllSlots(List<CalendarEvent> calendarEvents) {

    List<Slot> slots = Lists.newArrayList();
    CalendarEvent lastEvent = null;

    //  Iterate over the events two at a time and add all the blocked slots and free slots
    CalendarEvent first = calendarEvents.get(0);
    lastEvent = first;
    slots.add(new CalendarEventSlot(first, SlotType.BLOCKED_BY_USER));

    for (int index = 1; index < calendarEvents.size(); index++) {
      CalendarEvent second = calendarEvents.get(index);
      slots.add(new CalendarEventSlot(second, SlotType.BLOCKED_BY_USER));

      //  If the first and second events start together, the one with the higher end time will be
      //  second for sure due to sorting. So move ahead.
      if (first.getStartTime().compareTo(second.getStartTime()) == 0) {
        first = second;
        lastEvent = second;
      }
      //  If the first event starts before the second one, compare the end times
      else if (first.getStartTime() < second.getStartTime()) {
        //  If the first event ends before the second one starts, there is a free slot
        if (first.getEndTime() < second.getStartTime()) {
          slots.add(new SimpleSlot(first.getEndTime(), second.getStartTime(), SlotType.FREE));

          first = second;
          lastEvent = second;
        }
        //  If the first event ends after the second, skip the second one
        else if (first.getEndTime() > second.getEndTime()) {
          continue;
        }
        //  If the first one ends during the second one, there is no free slot, just move ahead
        else {
          first = second;
          lastEvent = second;
        }
      }
    }

    return SlottingResult.builder().slots(slots).lastEvent(lastEvent).build();
  }

  private static final class SlottingResult {

    private List<Slot> slots;
    private CalendarEvent lastEvent;

    public SlottingResult(List<Slot> slots, CalendarEvent lastEvent) {
      this.slots = slots;
      this.lastEvent = lastEvent;
    }

    public static Builder builder()  {
      return new Builder();
    }

    public List<Slot> getSlots() {
      return slots;
    }

    public CalendarEvent getLastEvent() {
      return lastEvent;
    }

    public static class Builder  {
      private List<Slot> slots;
      private CalendarEvent lastEvent;

      public Builder slots(List<Slot> slots)  {
        this.slots = slots;
        return this;
      }

      public Builder lastEvent(CalendarEvent lastEvent)  {
        this.lastEvent = lastEvent;
        return this;
      }

      public SlottingResult build() {
        return new SlottingResult(slots, lastEvent);
      }

    }

  }


}
