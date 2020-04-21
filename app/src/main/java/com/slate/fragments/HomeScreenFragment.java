package com.slate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.slate.activity.R;
import com.slate.dao.TaskDao;
import com.slate.models.task.Task;
import dagger.android.support.DaggerFragment;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Named;

public class HomeScreenFragment extends DaggerFragment {
  
  private final View.OnClickListener createTaskButtonListener;
  private final TaskDao taskDao;

  @Inject
  public HomeScreenFragment(@Named("CREATE_TASK") OnClickListener createTaskButtonListener,
      TaskDao taskDao) {
    this.createTaskButtonListener = createTaskButtonListener;
    this.taskDao = taskDao;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    Toast.makeText(getActivity(), "Home Screen Fragment", Toast.LENGTH_SHORT).show();
    return inflater.inflate(R.layout.home_fragment, container, false);
  }

  @Override
  public void onStart() {
    super.onStart();
    setNewTaskButtonListener();
    createTaskFragments();
  }

  private void setNewTaskButtonListener() {
    Optional.ofNullable(getActivity().findViewById(R.id.create_task_button))
        .ifPresent(view -> ((View) view).setOnClickListener(createTaskButtonListener));
  }

  private void createTaskFragments() {
    List<Task> allTasks = taskDao.getAllTasks();
    allTasks.forEach(task -> {
      if (!task.isCompleted()) {
        getChildFragmentManager().beginTransaction()
            .add(R.id.task_list_container,
                new CheckableTaskFragment(task, getTaskCompletionCallback()),
                "F" + task.getId())
            .commit();
      }
    });
  }

  private Function<Task, Void> getTaskCompletionCallback() {
    return task -> {
      taskDao.updateTask(task.getId(), task);
      createTaskFragments();
      return null;
    };
  }
}
