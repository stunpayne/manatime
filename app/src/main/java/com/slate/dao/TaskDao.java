package com.slate.dao;

import com.slate.models.task.Task;
import java.util.List;

/**
 * Data layer that provides CRUD operations on Task objects
 */
public interface TaskDao {

  boolean createTask(Task task);

  Task readTask(String taskId);

  List<Task> getAllTasks();

  boolean updateTask(String taskId, Task task);

  boolean deleteTask(String taskId);
}
