package com.slate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.slate.activity.R;
import dagger.android.support.DaggerFragment;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;

public class HomeScreenFragment extends DaggerFragment {

  private static final String TAG = HomeScreenFragment.class.getSimpleName();

  private final View.OnClickListener createTaskButtonListener;

  @Inject
  public HomeScreenFragment(@Named("CREATE_TASK") View.OnClickListener createTaskButtonListener) {
    this.createTaskButtonListener = createTaskButtonListener;
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
  }

  private void setNewTaskButtonListener() {
    Optional.ofNullable(getActivity().findViewById(R.id.create_task_button))
        .ifPresent(view -> ((View) view).setOnClickListener(createTaskButtonListener));
  }
}
