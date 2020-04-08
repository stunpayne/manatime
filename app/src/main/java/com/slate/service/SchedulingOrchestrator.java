package com.slate.service;


import com.google.common.collect.Lists;
import com.slate.common.MutableCalendar;
import com.slate.models.calendar.Calendar;
import com.slate.models.calendar.CalendarEvent;
import com.slate.models.calendar.CalendarEventRequest;
import com.slate.models.task.Task;
import com.slate.service.calendar.CalendarService;
import com.slate.service.scheduler.TaskScheduler;
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
    Calendar primaryCalendar = calendarService.getPrimaryCalendar(email);
    List<CalendarEvent> eventsTillDeadline = getEventsTillDeadline(primaryCalendar,
        task.getDeadline().getTime());
    List<Task> classifiedSlots = classifyTasks(eventsTillDeadline);
    Task scheduledTask = taskScheduler.schedule(task, eventsTillDeadline);
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
    calendarService.getCalendarEvents(createCalendarEventRequest(calendar.getId(), deadlineMillis));
    return Lists.newArrayList();
  }

  /**
   * Provides a calendar request for the given calendar ID and deadline
   */
  private CalendarEventRequest createCalendarEventRequest(String calendarId, Long deadlineMillis) {
    return CalendarEventRequest.builder().calendarId(calendarId).endTimeAfter(getMoratoriumEnd())
        .startTimeBefore(deadlineMillis).noLimit()
        .build();
  }

  /**
   * Returns the end time of the moratorium period - no events till the end of this period can be
   * rescheduled
   *
   * @return moratorium end time in epoch milliseconds
   */
  private Long getMoratoriumEnd() {
    return getNextDayNoon();
  }

  /**
   * Provides the epoch milliseconds till the next day noon
   */
  private Long getNextDayNoon() {
    return new MutableCalendar().addDays(1).setHour(12).getTimeInMillis();
  }

  //------------------------------------- Classifying events ------------------------------------//

  //  TODO: Change Task to Slot
  private List<Task> classifyTasks(List<CalendarEvent> presentEvents) {
    return Lists.newArrayList();
  }
}
