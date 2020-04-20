package com.slate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.slate.activity.R;
import com.slate.models.task.Task;
import dagger.android.support.DaggerFragment;

public class CheckableTaskFragment extends DaggerFragment {

  private final Task task;

  private CheckBox checkBox;
  private TextView textView;

  CheckableTaskFragment(Task task) {
    this.task = task;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_task_item, container, false);

    checkBox = view.findViewById(R.id.task_item_checkbox);
    textView = view.findViewById(R.id.task_item_name);

    textView.setText(task.getName());

    return view;
  }
}
