package com.slate.service.classifier;

import com.google.common.collect.Lists;
import com.slate.models.calendar.CalendarEvent;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EventSortingTest {

  private static SimpleSlotter simpleSlotter;

  @BeforeClass
  public static void setup() {
    simpleSlotter = new SimpleSlotter();
  }

  @Test
  public void calendarEvent_StartsBefore() {

    //  Test 2
    List<CalendarEvent> events2 = Lists.newArrayList(
        CalendarEvent.builder().id("1").startTime(110L).endTime(200L).build(),
        CalendarEvent.builder().id("2").startTime(100L).endTime(150L).build());
    simpleSlotter.sortEvents(events2);

    List<String> ids = Lists.newArrayList();
    events2.forEach(e -> ids.add(e.getId()));
    Assert.assertTrue(listEquals(ids, Lists.newArrayList("2", "1")));

    //  Test 2
    List<CalendarEvent> events3 = Lists.newArrayList(
        CalendarEvent.builder().id("1").startTime(110L).endTime(200L).build(),
        CalendarEvent.builder().id("2").startTime(100L).endTime(200L).build());
    simpleSlotter.sortEvents(events3);

    ids.clear();
    events3.forEach(e -> ids.add(e.getId()));
    Assert.assertTrue(listEquals(ids, Lists.newArrayList("2", "1")));
  }

  @Test
  public void calendarEvent_StartsSame_FirstSmaller() {
    List<CalendarEvent> events = Lists.newArrayList(
        CalendarEvent.builder().id("1").startTime(100L).endTime(200L).build(),
        CalendarEvent.builder().id("2").startTime(100L).endTime(250L).build());
    simpleSlotter.sortEvents(events);

    Assert.assertTrue(events.get(0).getId().equalsIgnoreCase("1"));
    Assert.assertTrue(events.get(1).getId().equalsIgnoreCase("2"));
  }

  @Test
  public void calendarEvent_StartsSame_FirstBigger() {
    List<CalendarEvent> events = Lists.newArrayList(
        CalendarEvent.builder().id("1").startTime(100L).endTime(250L).build(),
        CalendarEvent.builder().id("2").startTime(100L).endTime(200L).build());
    simpleSlotter.sortEvents(events);

    Assert.assertTrue(events.get(0).getId().equalsIgnoreCase("2"));
    Assert.assertTrue(events.get(1).getId().equalsIgnoreCase("1"));
  }

  @Test
  public void testFullyConsumed() {
    List<CalendarEvent> events = Lists.newArrayList(
        CalendarEvent.builder().id("1").startTime(100L).endTime(200L).build(),
        CalendarEvent.builder().id("2").startTime(50L).endTime(250L).build());
    simpleSlotter.sortEvents(events);

    Assert.assertTrue(events.get(0).getId().equalsIgnoreCase("2"));
    Assert.assertTrue(events.get(1).getId().equalsIgnoreCase("1"));
  }

  @Test
  public void complexTest1() {
    //  Input - (starting at 100 -> bigger, small, big), (fully consumed at 500 - consumed, consumer), (overlap at 800 - earlier, later)
    //  Output - (starting at 100 -> small, big, bigger .. fully consumed at 500 - consumer, consumed .. overlap at 800 - earlier, later

    List<CalendarEvent> events = Lists.newArrayList(
        CalendarEvent.builder().id("1").startTime(100L).endTime(300L).build(),
        CalendarEvent.builder().id("2").startTime(100L).endTime(200L).build(),
        CalendarEvent.builder().id("3").startTime(100L).endTime(250L).build(),

        CalendarEvent.builder().id("4").startTime(550L).endTime(600L).build(),
        CalendarEvent.builder().id("5").startTime(500L).endTime(700L).build(),

        CalendarEvent.builder().id("6").startTime(800L).endTime(1000L).build(),
        CalendarEvent.builder().id("7").startTime(900L).endTime(1100L).build());
    simpleSlotter.sortEvents(events);

    List<String> ids = Lists.newArrayList();
    events.forEach(e -> ids.add(e.getId()));
    Assert.assertTrue(listEquals(ids, Lists.newArrayList("2", "3", "1", "5", "4", "6", "7")));
  }

  @Test
  public void complexTest2() {
    //  Input - (starting at 100 -> bigger, small, big, big consumer), (fully consumed at 500 - consumed, consumer), (overlap at 800 - earlier, later)
    //  Output - (starting at 100 -> small, big, bigger .. fully consumed at 500 - consumer, consumed .. overlap at 800 - earlier, later

    List<CalendarEvent> events = Lists.newArrayList(
        CalendarEvent.builder().id("1").startTime(100L).endTime(300L).build(),
        CalendarEvent.builder().id("2").startTime(100L).endTime(200L).build(),
        CalendarEvent.builder().id("3").startTime(100L).endTime(250L).build(),
        CalendarEvent.builder().id("4").startTime(50L).endTime(800L).build(),

        CalendarEvent.builder().id("5").startTime(550L).endTime(600L).build(),
        CalendarEvent.builder().id("6").startTime(500L).endTime(700L).build(),

        CalendarEvent.builder().id("7").startTime(900L).endTime(1100L).build(),
        CalendarEvent.builder().id("8").startTime(800L).endTime(1000L).build());
    simpleSlotter.sortEvents(events);

    List<String> ids = Lists.newArrayList();
    events.forEach(e -> ids.add(e.getId()));
    Assert.assertTrue(listEquals(ids, Lists.newArrayList("4", "2", "3", "1", "6", "5", "8", "7")));
  }


  private <T> boolean listEquals(List<T> list1, List<T> list2) {
    if (list1.size() != list2.size()) {
      return false;
    }

    for (int i = 0; i < list1.size(); i++) {
      if (!list1.get(i).equals(list2.get(i))) {
        return false;
      }
    }

    return true;
  }

}