package com.slate.service;


import android.util.Log;
import com.google.common.collect.Lists;
import com.slate.common.MutableCalendar;
import com.slate.exception.SchedulingException;
import com.slate.models.calendar.Calendar;
import com.slate.models.calendar.CalendarEvent;
import com.slate.models.calendar.CalendarEventRequest;
import com.slate.models.slot.ScheduleSlot;
import com.slate.models.slot.SimpleSlot;
import com.slate.models.slot.Slot;
import com.slate.models.slot.SlotType;
import com.slate.models.task.Task;
import com.slate.service.calendar.CalendarService;
import com.slate.service.scheduler.TaskScheduler;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 * Orchestrates the process of scheduling a new task on a user's calendar. At a high level, first it
 * gets the current tasks on the calendar from the end of the moratorium window to the task's
 * deadline. Then, creates different time slots and uses a TaskScheduler to schedule the task on the
 * user's calendar
 *
 * @see TaskScheduler
 */
public class SchedulingOrchestrator {

  private static final String TAG = SchedulingOrchestrator.class.getSimpleName();

  private final CalendarService calendarService;
  private final TaskScheduler taskScheduler;

  @Inject
  public SchedulingOrchestrator(CalendarService calendarService,
      TaskScheduler taskScheduler) {
    this.calendarService = calendarService;
    this.taskScheduler = taskScheduler;
  }

  /**
   * Schedules a new task on the user's calendar
   *
   * @param email the user's email linked to the calendar, used to fetch the current events on the
   *              calendar and for scheduling the new task
   * @param task  the new task to schedule on the calendar
   */
  public void scheduleTask(String email, Task task) {
    //  Get the user's calendar
    Calendar primaryCalendar = calendarService.getPrimaryCalendar(email);

    //  Get all the events on the calendar till deadline
    List<CalendarEvent> eventsTillDeadline = getEventsTillDeadline(primaryCalendar,
        task.getDeadline().getTime());

    //  Classify all the events and free blocks into slots
    List<Slot> classifiedSlots = classifyTasks(eventsTillDeadline);

    //  Get time slot during which event is to be scheduled
    CalendarEvent eventToSchedule = scheduleAndGetEvent(email, task, primaryCalendar);
    Log.d(TAG, "Event to schedule: " + eventToSchedule);

    //  Make an entry into the calendar
    CalendarEvent scheduledEvent = calendarService.addCalendarEvent(eventToSchedule);
    Log.d(TAG, "Scheduled event: " + scheduledEvent);
  }

  private CalendarEvent scheduleAndGetEvent(String email, Task task, Calendar calendar) {
    try {
      ScheduleSlot slot = taskScheduler
          .schedule(task, DummyTimeSlotGenerator.generate(task.getDeadline().getTime()));
      return createCalendarEvent(task, email, slot, calendar);
    } catch (SchedulingException e) {
      Log.e(TAG, "Error occurred while scheduling task", e);
      return null;
    }
  }

  //-------------------------------------- Fetching events --------------------------------------//

  /**
   * Returns all events on the user's calendar from the end of moratorium to the new task deadline
   *
   * @param calendar       a reference to the user's calendar
   * @param deadlineMillis the deadline (in milliseconds) of the new task
   * @return a list of the events on the user's calendar
   */
  private List<CalendarEvent> getEventsTillDeadline(Calendar calendar, Long deadlineMillis) {
    return calendarService
        .getCalendarEvents(createCalendarEventRequest(calendar.getId(), deadlineMillis));
  }

  /**
   * Provides a calendar request for the given calendar ID and deadline
   */
  private CalendarEventRequest createCalendarEventRequest(String calendarId, Long deadlineMillis) {
    return CalendarEventRequest.builder()
        .calendarId(calendarId)
        .endTimeAfter(getMoratoriumEnd())
        .startTimeBefore(deadlineMillis)
        .build();
  }

  /**
   * Returns the end time of the moratorium period - no events till the end of this period can be
   * rescheduled
   *
   * @return moratorium end time in epoch milliseconds
   */
  private static Long getMoratoriumEnd() {
    return getNextDayNoon();
  }

  /**
   * Provides the epoch milliseconds till the next day noon
   */
  private static Long getNextDayNoon() {
    return new MutableCalendar().addDays(1).setExactHour(12).getTimeInMillis();
  }

  //------------------------------------- Classifying events ------------------------------------//

  private List<Slot> classifyTasks(List<CalendarEvent> presentEvents) {
    return Lists.newArrayList();
  }

  private static class DummyTimeSlotGenerator {

    static List<Slot> generate(long deadline) {
      long firstAvailable = getMoratoriumEnd();
      long lastAvailable = (firstAvailable + deadline) / 2;

      Slot freeSlot = new SimpleSlot(firstAvailable, lastAvailable, SlotType.FREE);
      Slot blockedSlot = new SimpleSlot(lastAvailable, deadline, SlotType.BLOCKED_BY_USER);

      return Lists.newArrayList(freeSlot, blockedSlot);
    }
  }

  /**
   * Creates a calendar event from the task in the slot given by the TaskScheduler
   *
   * @return a CalendarEvent for the task to be created
   */
  private CalendarEvent createCalendarEvent(Task task, String email, ScheduleSlot slot,
      Calendar calendar) {

    //  TODO: Set Location of task
    return CalendarEvent.builder()
        .calendarId(calendar.getId())
        .organizer(email)
        .title(task.getName())
        .description(task.getDescription())
        .startTime(slot.getStartTime())
        .endTime(slot.getEndTime())
        .timeZone(calendar.getTimeZone())
        .location(null)
        .build();
  }
}
