package com.slate.service.scheduler;

import com.slate.models.calendar.CalendarEvent;
import com.slate.models.task.Task;
import java.util.List;

/**
 * Schedules a given task during the first free slot it finds. Not meant to be used on production,
 * just a simple test of the scheduling functionality.
 */
public class FirstSlotTaskScheduler implements TaskScheduler {

  @Override
  public Task schedule(Task task, List<CalendarEvent> calendarEvents) {
    return null;
  }
}
