package com.slate.service.classifier;

import com.google.api.client.util.Lists;
import com.slate.models.calendar.CalendarEvent;
import com.slate.models.slot.Slot;
import com.slate.models.slot.SlotType;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SlotCreationTest {

  private static SimpleSlotter simpleSlotter;

  private static Long startTime = 1000L;
  private static Long deadline = 9000L;

  @BeforeClass
  public static void setup() {
    simpleSlotter = new SimpleSlotter();
  }

  @Test
  public void noEvent() {
    List<CalendarEvent> events = Lists.newArrayList();
    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    checkSlotValues(SlotType.FREE, startTime, deadline, allSlots.get(0), "1");
  }

  @Test
  public void oneEvent() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(2000L).endTime(5000L).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all three slots
    Assert.assertEquals("Expected 3 slots", 3, allSlots.size());

    checkSlotValues(SlotType.FREE, startTime, 2000L, allSlots.get(0), "1");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 5000L, allSlots.get(1), "2");
    checkSlotValues(SlotType.FREE, 5000L, deadline, allSlots.get(2), "3");
  }

  @Test
  public void oneEventEndingAtDeadline() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(2000L).endTime(deadline).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all three slots
    Assert.assertEquals("Expected 2 slots", 2, allSlots.size());

    checkSlotValues(SlotType.FREE, startTime, 2000L, allSlots.get(0), "1");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, deadline, allSlots.get(1), "1");
  }

  @Test
  public void oneEventStartingAtStartTime() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(startTime).endTime(5000L).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all three slots
    Assert.assertEquals("Expected 2 slots", 2, allSlots.size());

    checkSlotValues(SlotType.BLOCKED_BY_USER, startTime, 5000L, allSlots.get(0), "1");
    checkSlotValues(SlotType.FREE, 5000L, deadline, allSlots.get(1), "2");
  }

  @Test
  public void oneAllOccupyingEvent() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(startTime).endTime(deadline).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all three slots
    Assert.assertEquals("Expected 1 slots", 1, allSlots.size());

    checkSlotValues(SlotType.BLOCKED_BY_USER, startTime, deadline, allSlots.get(0), "1");
  }

  @Test
  public void oneEventBiggerThanWindow() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(startTime - 1L).endTime(deadline + 1L).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all slots
    Assert.assertEquals("Expected 1 slots", 1, allSlots.size());

    checkSlotValues(SlotType.BLOCKED_BY_USER, startTime - 1L, deadline + 1L, allSlots.get(0), "1");
  }

  @Test
  public void twoEventsStartTogether_EndTogether() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(2000L).endTime(4000L).build());
    events.add(CalendarEvent.builder().startTime(2000L).endTime(4000L).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all 4 slots
    Assert.assertEquals("Expected 4 slots", 4, allSlots.size());

    checkSlotValues(SlotType.FREE, startTime, 2000L, allSlots.get(0), "1");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 4000L, allSlots.get(1), "2");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 4000L, allSlots.get(2), "3");
    checkSlotValues(SlotType.FREE, 4000L, deadline, allSlots.get(3), "4");
  }

  @Test
  public void twoEventsStartTogether_SecondEndsLater() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(2000L).endTime(4000L).build());
    events.add(CalendarEvent.builder().startTime(2000L).endTime(5000L).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all 4 slots
    Assert.assertEquals("Expected 4 slots", 4, allSlots.size());

    checkSlotValues(SlotType.FREE, startTime, 2000L, allSlots.get(0), "1");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 4000L, allSlots.get(1), "2");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 5000L, allSlots.get(2), "3");
    checkSlotValues(SlotType.FREE, 5000L, deadline, allSlots.get(3), "4");
  }

  @Test
  public void twoAdjacentEvents() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(2000L).endTime(4000L).build());
    events.add(CalendarEvent.builder().startTime(4000L).endTime(7000L).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all 4 slots
    Assert.assertEquals("Expected 4 slots", 4, allSlots.size());

    checkSlotValues(SlotType.FREE, startTime, 2000L, allSlots.get(0), "1");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 4000L, allSlots.get(1), "2");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 4000L, 7000L, allSlots.get(2), "3");
    checkSlotValues(SlotType.FREE, 7000L, deadline, allSlots.get(3), "4");
  }

  @Test
  public void twoSeparateEvents() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(2000L).endTime(4000L).build());
    events.add(CalendarEvent.builder().startTime(5000L).endTime(7000L).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all 4 slots
    Assert.assertEquals("Expected 5 slots", 5, allSlots.size());

    checkSlotValues(SlotType.FREE, startTime, 2000L, allSlots.get(0), "1");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 4000L, allSlots.get(1), "2");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 5000L, 7000L, allSlots.get(2), "3");
    checkSlotValues(SlotType.FREE, 4000L, 5000L, allSlots.get(3), "4");
    checkSlotValues(SlotType.FREE, 7000L, deadline, allSlots.get(4), "5");
  }

  @Test
  public void twoOverlappingEvents() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(2000L).endTime(4000L).build());
    events.add(CalendarEvent.builder().startTime(4000L).endTime(7000L).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all 4 slots
    Assert.assertEquals("Expected 5 slots", 4, allSlots.size());

    checkSlotValues(SlotType.FREE, startTime, 2000L, allSlots.get(0), "1");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 4000L, allSlots.get(1), "2");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 4000L, 7000L, allSlots.get(2), "3");
    checkSlotValues(SlotType.FREE, 7000L, deadline, allSlots.get(3), "4");
  }

  @Test
  public void twoEvents_OneConsuming() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(2000L).endTime(7000L).build());
    events.add(CalendarEvent.builder().startTime(4000L).endTime(5000L).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all 4 slots
    Assert.assertEquals("Expected 5 slots", 4, allSlots.size());

    checkSlotValues(SlotType.FREE, startTime, 2000L, allSlots.get(0), "1");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 7000L, allSlots.get(1), "2");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 4000L, 5000L, allSlots.get(2), "3");
    checkSlotValues(SlotType.FREE, 7000L, deadline, allSlots.get(3), "4");
  }

  @Test
  public void fourEvents_StartSame_MultipleOverlap() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(2000L).endTime(4000L).build());
    events.add(CalendarEvent.builder().startTime(2000L).endTime(5000L).build());
    events.add(CalendarEvent.builder().startTime(4000L).endTime(5000L).build());
    events.add(CalendarEvent.builder().startTime(4000L).endTime(6000L).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all 4 slots
    Assert.assertEquals("Expected 6 slots", 6, allSlots.size());

    checkSlotValues(SlotType.FREE, startTime, 2000L, allSlots.get(0), "1");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 4000L, allSlots.get(1), "2");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 5000L, allSlots.get(2), "2");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 4000L, 5000L, allSlots.get(3), "3");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 4000L, 6000L, allSlots.get(4), "3");
    checkSlotValues(SlotType.FREE, 6000L, deadline, allSlots.get(5), "4");
  }

  @Test
  public void eightEvents_StartSame_MultipleOverlap_Consume() {
    List<CalendarEvent> events = Lists.newArrayList();
    events.add(CalendarEvent.builder().startTime(2000L).endTime(4000L).build());
    events.add(CalendarEvent.builder().startTime(2000L).endTime(5000L).build());
    events.add(CalendarEvent.builder().startTime(4000L).endTime(5000L).build());
    events.add(CalendarEvent.builder().startTime(4000L).endTime(6000L).build()); // block ends
    events.add(CalendarEvent.builder().startTime(6500L).endTime(8500L).build());
    events.add(CalendarEvent.builder().startTime(7000L).endTime(8000L).build());
    events.add(CalendarEvent.builder().startTime(8000L).endTime(8500L).build());
    events.add(CalendarEvent.builder().startTime(8500L).endTime(8800L).build());

    List<Slot> allSlots = simpleSlotter.createSlots(startTime, deadline, events);

    //  Verify all 4 slots
    Assert.assertEquals("Expected 11 slots", 11, allSlots.size());

    checkSlotValues(SlotType.FREE, startTime, 2000L, allSlots.get(0), "1");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 4000L, allSlots.get(1), "2");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 2000L, 5000L, allSlots.get(2), "3");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 4000L, 5000L, allSlots.get(3), "4");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 4000L, 6000L, allSlots.get(4), "5");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 6500L, 8500L, allSlots.get(5), "6");
    checkSlotValues(SlotType.FREE, 6000L, 6500L, allSlots.get(6), "7");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 7000L, 8000L, allSlots.get(7), "8");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 8000L, 8500L, allSlots.get(8), "9");
    checkSlotValues(SlotType.BLOCKED_BY_USER, 8500L, 8800L, allSlots.get(9), "10");
    checkSlotValues(SlotType.FREE, 8800L, deadline, allSlots.get(10), "11");
  }


  private void checkSlotValues(SlotType slotType, Long startTime, Long endTime, Slot slot,
      String logId) {
    Assert.assertEquals("Wrong start time " + logId, startTime, slot.getStartTime());
    Assert.assertEquals("Wrong end time " + logId, endTime, slot.getEndTime());
    Assert.assertEquals("Wrong slot type " + logId, slotType, slot.getType());
  }

}