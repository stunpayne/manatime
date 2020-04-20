package com.slate.dao;

import android.content.Context;
import android.util.Log;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.slate.common.Constants.Files;
import com.slate.common.FileUtil;
import com.slate.models.task.Task;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;

public class InternalStorageTaskDao implements TaskDao {

  private static final String TAG = InternalStorageTaskDao.class.getSimpleName();

  private final Context context;
  private Map<String, Task> taskMap;

  @Inject
  public InternalStorageTaskDao(Context context) {
    this.context = context;
    this.taskMap = initialize(context);
  }

  @Override
  public boolean createTask(Task newTask) {
    Optional.ofNullable(newTask)
        .ifPresent(task -> taskMap.put(task.getId(), task));
    return FileUtil.writeObjectToFile(context, Files.TASK_FILE_NAME, taskMap);
  }

  @Override
  public Task readTask(String taskId) {
    return taskMap.get(taskId);
  }

  @Override
  public List<Task> getAllTasks() {
    return Lists.newArrayList(taskMap.values());
  }

  @Override
  public boolean updateTask(String taskId, Task task) {
    taskMap.put(taskId, task);
    return true;
  }

  @Override
  public boolean deleteTask(String taskId) {
    taskMap.remove(taskId);
    return true;
  }

  /**
   * Loads the task file containing all the serialized tasks and creates a hash map of all tasks
   *
   * @param context the application context
   * @return a hash map containing task ID to task mappings
   */
  private static Map<String, Task> initialize(Context context) {
    Map<String, Task> deseredTaskMap = FileUtil
        .readFile(context, Files.TASK_FILE_NAME, new TypeToken<Map<String, Task>>() {
        });
    if (Objects.isNull(deseredTaskMap)) {
      deseredTaskMap = Maps.newHashMap();
    }
    Log.d(TAG, "Map read from file " + deseredTaskMap);
    return deseredTaskMap;
  }
}
