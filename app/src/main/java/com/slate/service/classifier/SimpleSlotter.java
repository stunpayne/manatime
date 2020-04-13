package com.slate.service.classifier;

import com.google.common.collect.Lists;
import com.slate.models.calendar.CalendarEvent;
import com.slate.models.slot.CalendarEventSlot;
import com.slate.models.slot.SimpleSlot;
import com.slate.models.slot.Slot;
import com.slate.models.slot.SlotType;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SimpleSlotter implements Slotter {

  private static final String TAG = SimpleSlotter.class.getSimpleName();

  private static final Comparator<CalendarEvent> timeComparator = (first, second) ->{
    if (first.getStartTime() < second.getStartTime())
        return -1;

    if (first.getStartTime().equals(second.getStartTime()))
      return first.getEndTime().compareTo(second.getEndTime());

    return 1;
  };

  public void sortEvents(List<CalendarEvent> events)  {
    events.sort(timeComparator);
  }

  @Override
  public List<Slot> createSlots(Long startTime, Long deadline, List<CalendarEvent> calendarEvents) {

    //  If there are no events, the entire time duration is a free slot
    if (calendarEvents.size() == 0) {
      return Lists.newArrayList(new SimpleSlot(startTime, deadline, SlotType.FREE));
    } else if (calendarEvents.size() == 1) {
      //  TODO: Check if this event is scheduled by us, then mark as OCCUPIED
      SlotType slotType = SlotType.BLOCKED_BY_USER;
      return Lists.newArrayList(new CalendarEventSlot(calendarEvents.get(0), slotType));
    }

    List<Slot> timeSlots = Lists.newArrayList();

    //  Sort events in ascending order of start time
    calendarEvents.sort(timeComparator);

    //  If the start time is before the time the first event starts at, there is a free slot
    if (startTime < calendarEvents.get(0).getStartTime()) {
      timeSlots.add(new SimpleSlot(startTime, calendarEvents.get(0).getStartTime(), SlotType.FREE));
    }

    CalendarEvent currentBiggest = null;
    Slot slot = null;
//    slotCreationOne(calendarEvents, currentBiggest, slot);

    createAllSlots(calendarEvents);

    //  If the last event end at the deadline moratorium, do not create a slot for that
    if (startTime < calendarEvents.get(0).getStartTime()) {
      timeSlots.add(new SimpleSlot(startTime, calendarEvents.get(0).getStartTime(), SlotType.FREE));
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
  private List<Slot> createAllSlots(List<CalendarEvent> calendarEvents) {

    List<Slot> slots = Lists.newArrayList();

    //  Iterate over the events two at a time and add all the blocked slots and free slots
    CalendarEvent first = calendarEvents.get(0);
    slots.add(new CalendarEventSlot(first, SlotType.BLOCKED_BY_USER));

    for (int index = 1; index < calendarEvents.size(); index++) {
      CalendarEvent second = calendarEvents.get(index);
      slots.add(new CalendarEventSlot(second, SlotType.BLOCKED_BY_USER));

      //  If the first event ends before the second one starts, there is a free slot in between
      if (first.getEndTime().compareTo(second.getStartTime()) < 0) {
        slots.add(new SimpleSlot(first.getEndTime(), second.getStartTime(), SlotType.FREE));
      }
    }

    return slots;
  }


  private void slotCreationOne(List<CalendarEvent> calendarEvents, CalendarEvent currentBiggest,
      Slot slot) {
    for (Iterator<CalendarEvent> iterator = calendarEvents.iterator(); iterator.hasNext(); ) {
      if (Objects.isNull(currentBiggest)) {
        currentBiggest = iterator.next();
        //  TODO: Understand blocked by user vs slated here
        slot = new SimpleSlot(currentBiggest.getStartTime(), currentBiggest.getEndTime(),
            SlotType.BLOCKED_BY_USER);
      }

      CalendarEvent curr = iterator.next();
      if (currentBiggest.getStartTime() < curr.getStartTime()) {
        if (currentBiggest.getEndTime() > curr.getEndTime()) {
          continue;
        }
        if (currentBiggest.getEndTime() < curr.getEndTime()) {
          slot.setEndTime(curr.getEndTime());
          currentBiggest = curr;
        }
      }

      if (currentBiggest.getStartTime() == curr.getStartTime()) {
        if (currentBiggest.getEndTime() < curr.getEndTime()) {
          slot.setEndTime(curr.getEndTime());
          currentBiggest = curr;
        }
      }
    }
  }

}
