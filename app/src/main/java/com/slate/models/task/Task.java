package com.slate.models.task;

import java.util.Date;

/**
 * A schedulable task object. This class contains all the information about a task that is needed to
 * schedule it.
 */
public class Task {

  private String name;
  private TaskType taskType;
  private String description;

  private Date deadline;
  private Long durationMinutes;
  private Importance importance;

  private Reward reward;

  public static Builder builder() {
    return new Builder();
  }

  public String getName() {
    return name;
  }

  public TaskType getTaskType() {
    return taskType;
  }

  public String getDescription() {
    return description;
  }

  public Date getDeadline() {
    return deadline;
  }

  public Long getDurationMinutes() {
    return durationMinutes;
  }

  public Long getDurationMillis() {
    return durationMinutes * 60 * 1000L;
  }

  public Importance getImportance() {
    return importance;
  }

  public Reward getReward() {
    return reward;
  }

  public static final class Builder {

    private String name;
    private TaskType taskType;
    private String description;
    private Date deadline;
    private Long durationMinutes;
    private Importance importance;
    private Reward reward;

    private Builder() {
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder taskType(TaskType taskType) {
      this.taskType = taskType;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder deadline(Date deadline) {
      this.deadline = deadline;
      return this;
    }

    public Builder durationMinutes(Long durationMinutes) {
      this.durationMinutes = durationMinutes;
      return this;
    }

    public Builder importance(Importance importance) {
      this.importance = importance;
      return this;
    }

    public Builder reward(Reward reward) {
      this.reward = reward;
      return this;
    }

    public Task build() {
      Task task = new Task();
      task.taskType = this.taskType;
      task.description = this.description;
      task.deadline = this.deadline;
      task.durationMinutes = this.durationMinutes;
      task.reward = this.reward;
      task.name = this.name;
      task.importance = this.importance;
      return task;
    }
  }

  @Override
  public String toString() {
    return "{\"name\" : " + (name == null ? null : "\"" + name + "\"") + ",\"taskType\" : " + (
        taskType == null ? null : taskType) + ",\"description\" : " + (description == null ? null
        : "\"" + description + "\"") + ",\"deadline\" : " + (deadline == null ? null : deadline)
        + ",\"durationMinutes\" : " + (durationMinutes == null ? null : durationMinutes)
        + ",\"importance\" : " + (importance == null ? null : importance) + ",\"reward\" : " + (
        reward == null ? null : reward) + "}";
  }
}
