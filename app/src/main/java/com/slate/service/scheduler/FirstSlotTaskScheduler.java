package com.slate.service.scheduler;

import com.slate.exception.SchedulingException;
import com.slate.models.slot.ScheduleSlot;
import com.slate.models.slot.Slot;
import com.slate.models.slot.SlotType;
import com.slate.models.task.Task;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

/**
 * Schedules a given task during the first free slot it finds. Not meant to be used on production,
 * just a simple test of the scheduling functionality.
 */
public class FirstSlotTaskScheduler implements TaskScheduler {

  private static final String NO_FREE_SLOT = "No free slot found!";

  @Inject
  public FirstSlotTaskScheduler() {
  }

  @Override
  public ScheduleSlot schedule(Task task, List<Slot> calendarSlots) throws SchedulingException {
    return
        Optional.ofNullable(findFirstFreeSlot(calendarSlots))
            .map(slot -> new ScheduleSlot(slot.getStartTime(),
                slot.getStartTime() + task.getDurationMillis()))
            .orElseThrow(() -> new SchedulingException(NO_FREE_SLOT));
  }

  private Slot findFirstFreeSlot(List<Slot> calendarSlots) {
    for (Slot slot : calendarSlots) {
      if (slot.getType() == SlotType.FREE) {
        return slot;
      }
    }
    return null;
  }
}
