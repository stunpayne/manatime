package com.slate.service.scheduler;

import com.slate.models.calendar.CalendarEvent;
import com.slate.models.task.Task;
import java.util.List;

/**
 * Schedules a task given some details about the task
 */
public interface TaskScheduler {

  /**
   * Schedules a task given some details about the task and returns details of the scheduled task
   *
   * @param task           the task to schedule
   * @param calendarEvents a list of events already scheduled on the calendar
   * @return a Task containing same details as the input task, along with the scheduled time
   */
  Task schedule(Task task, List<CalendarEvent> calendarEvents);
}
