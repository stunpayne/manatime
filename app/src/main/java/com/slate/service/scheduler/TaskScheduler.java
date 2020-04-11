package com.slate.service.scheduler;

import com.slate.exception.SchedulingException;
import com.slate.models.calendar.CalendarEvent;
import com.slate.models.slot.ScheduleSlot;
import com.slate.models.slot.Slot;
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
   * @param calendarSlots a list of slots on the user's calendar
   * @return a Task containing same details as the input task, along with the scheduled time
   */
  ScheduleSlot schedule(Task task, List<Slot> calendarSlots) throws SchedulingException;
}
