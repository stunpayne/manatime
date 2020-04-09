package com.slate.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.slate.activity.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
import dagger.android.support.DaggerDialogFragment;
import java.util.Calendar;
import javax.inject.Inject;

public class CreateTaskFragment extends DaggerDialogFragment implements OnTimeSetListener,
    OnDateSetListener {

  private static final String TAG = CreateTaskFragment.class.getSimpleName();
  private static final double WIDTH_PERCENT = 0.75;

  @Inject
  public CreateTaskFragment() {
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    Toast.makeText(getActivity(), "Create Task Fragment", Toast.LENGTH_SHORT).show();
    return inflater.inflate(R.layout.create_task_fragment, container, false);
  }

  @Override
  public void onStart() {
    super.onStart();

    Calendar now = Calendar.getInstance();
    getDialog().findViewById(R.id.create_task_dl_date_actual).setOnClickListener(v -> {
      DatePickerDialog datePicker = DatePickerDialog.newInstance(null,
          now.get(Calendar.YEAR), // Initial year selection
          now.get(Calendar.MONTH), // Initial month selection
          now.get(Calendar.DAY_OF_MONTH) // Initial day selection
      );
      datePicker.setVersion(DatePickerDialog.Version.VERSION_2);
      datePicker.setOnDateSetListener(this);
      datePicker.show(getActivity().getSupportFragmentManager(), "Date_Picker");
    });

    getDialog().findViewById(R.id.create_task_dl_time_actual).setOnClickListener(v -> {
      TimePickerDialog timePicker = TimePickerDialog.newInstance(null,
          now.get(Calendar.HOUR),
          now.get(Calendar.MINUTE),
          now.get(Calendar.SECOND),
          true
      );
      timePicker.setVersion(TimePickerDialog.Version.VERSION_2);
      timePicker.setOnTimeSetListener(this);
      timePicker.show(getActivity().getSupportFragmentManager(), "Date_Picker");
    });
  }

  @Override
  public void onResume() {
    // Store access variables for window and blank point
    Window window = getDialog().getWindow();
    Point size = new Point();
    // Store dimensions of the screen in `size`
    Display display = window.getWindowManager().getDefaultDisplay();
    display.getSize(size);
    // Set the width of the dialog proportional to 75% of the screen width
    window.setLayout((int) (size.x * WIDTH_PERCENT), WindowManager.LayoutParams.WRAP_CONTENT);
    window.setGravity(Gravity.CENTER);
    // Call super onResume after sizing
    super.onResume();
  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    Log.d(TAG,
        "You picked the following date: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
  }

  @Override
  public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
    Log.d(TAG, "You picked the following time: " + hourOfDay + "h" + minute + "m" + second);
  }
}
