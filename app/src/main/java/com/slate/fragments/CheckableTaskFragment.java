package com.slate.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.slate.activity.R;
import com.slate.models.task.Task;
import dagger.android.support.DaggerFragment;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public class CheckableTaskFragment extends DaggerFragment {

  private static final String TAG = CheckableTaskFragment.class.getSimpleName();

  private final Task task;
  private final Function<Task, Void> taskCompletionCallback;

  CheckableTaskFragment(Task task,
      Function<Task, Void> taskCompletionCallback) {
    this.task = task;
    this.taskCompletionCallback = taskCompletionCallback;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_task_item, container, false);

    TextView textView = view.findViewById(R.id.task_item_name);
    textView.setText(task.getName());

    CheckBox checkBox = view.findViewById(R.id.task_item_checkbox);
    checkBox.setOnClickListener(getCheckBoxOnClickListener());

    return view;
  }

  @NotNull
  private OnClickListener getCheckBoxOnClickListener() {
    return v -> {
      task.markCompleted();
      try {
        taskCompletionCallback.apply(this.task);
      } catch (Exception e) {
        Log.e(TAG, "Error occurred while completing task: ", e);
      }
    };
  }
}
