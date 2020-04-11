package com.slate.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import com.slate.activity.R;
import com.slate.models.task.Task;
import com.slate.models.user.SignedInUser;
import com.slate.service.SchedulingOrchestrator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
import dagger.android.support.DaggerDialogFragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import org.jetbrains.annotations.NotNull;

public class CreateTaskFragment extends DaggerDialogFragment implements OnTimeSetListener,
    OnDateSetListener {

  private static final String TAG = CreateTaskFragment.class.getSimpleName();
  private static final double WIDTH_PERCENT = 0.85;
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
  private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
  public static final String EVENT_SCHEDULED = "Event scheduled! Check your Calendar!";

  private final SchedulingOrchestrator schedulingOrchestrator;
  private final SignedInUser signedInUser;

  private EditText deadlineDate;
  private EditText deadlineTime;

  public CreateTaskFragment(SchedulingOrchestrator schedulingOrchestrator,
      SignedInUser signedInUser) {
    this.schedulingOrchestrator = schedulingOrchestrator;
    this.signedInUser = signedInUser;
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
    deadlineDate = getDialog().findViewById(R.id.create_task_dl_date_actual);
    deadlineDate.setOnClickListener(getDateOnClickListener(now));

    deadlineTime = getDialog().findViewById(R.id.create_task_dl_time_actual);
    deadlineTime.setOnClickListener(getTimeOnClickListener(now));

    getDialog().findViewById(R.id.submit_task)
        .setOnClickListener(getButtonOnClickListener());
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
    int month = monthOfYear + 1;
    String date = getActivity()
        .getString(R.string.dl_date_fill, String.valueOf(month), String.valueOf(dayOfMonth),
            String.valueOf(year));
    Log.d(TAG, "You picked the following date: " + date);
    deadlineDate.setText(date);
  }

  @Override
  public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
    String time = getActivity()
        .getString(R.string.dl_time_fill, String.valueOf(hourOfDay), String.valueOf(minute),
            String.valueOf(second));
    Log.d(TAG, "You picked the following time: " + time);
    deadlineTime.setText(time);
  }

  @NotNull
  private OnClickListener getDateOnClickListener(Calendar time) {
    return v -> {
      DatePickerDialog datePicker = DatePickerDialog.newInstance(null,
          time.get(Calendar.YEAR), // Initial year selection
          time.get(Calendar.MONTH), // Initial month selection
          time.get(Calendar.DAY_OF_MONTH) // Initial day selection
      );
      datePicker.setVersion(DatePickerDialog.Version.VERSION_2);
      datePicker.setOnDateSetListener(this);
      datePicker.show(getActivity().getSupportFragmentManager(), "Date_Picker");
    };
  }

  @NotNull
  private OnClickListener getTimeOnClickListener(Calendar time) {
    return v -> {
      TimePickerDialog timePicker = TimePickerDialog.newInstance(null,
          time.get(Calendar.HOUR),
          time.get(Calendar.MINUTE),
          time.get(Calendar.SECOND),
          true
      );
      timePicker.setVersion(TimePickerDialog.Version.VERSION_2);
      timePicker.setOnTimeSetListener(this);
      timePicker.show(getActivity().getSupportFragmentManager(), "Time_Picker");
    };
  }

  @NotNull
  private OnClickListener getButtonOnClickListener() {
    return v -> {
      String taskName = getTextInputLayoutText(R.id.create_task_name);
      String deadlineDate = getTextInputLayoutText(R.id.create_task_dl_date);
      String deadlineTime = getTextInputLayoutText(R.id.create_task_dl_time);
      Integer duration = Integer.valueOf(getTextInputLayoutText(R.id.create_task_duration));
      Task newTask = createTask(taskName, deadlineDate, deadlineTime, duration);

      Log.d(TAG, "getButtonOnClickListener: Task attributed received!");

      this.schedulingOrchestrator.scheduleTask(signedInUser.getEmail(), newTask);
      Toast.makeText(getActivity(), EVENT_SCHEDULED, Toast.LENGTH_SHORT).show();
      dismiss();
    };
  }

  /**
   * Returns the text content from any Material TextInputLayout view
   *
   * @param textInputLayoutResourceId the resource ID of the material text field
   * @return the text inside the material text field
   */
  private String getTextInputLayoutText(int textInputLayoutResourceId) {
    return ((TextInputLayout) getDialog().findViewById(textInputLayoutResourceId)).getEditText()
        .getText().toString();
  }

  /**
   * Returns a task object that can be scheduled on the user's calendar
   */
  private Task createTask(String taskName, String deadlineDate, String deadlineTime,
      Integer duration) {
    Calendar deadline = getDeadlineDateTime(deadlineDate, deadlineTime);

    return Task.builder()
        .name(taskName)
        .deadline(deadline.getTime())
        .durationMinutes(Long.valueOf(duration))
        .build();
  }

  /**
   * Combines the separate deadline date and deadline time into one object
   *
   * @param deadlineDate the deadline Date
   * @param deadlineTime the deadline Time
   * @return a Calendar with the date and time set as that of the deadline
   */
  @NotNull
  private Calendar getDeadlineDateTime(String deadlineDate, String deadlineTime) {
    Calendar deadlineTimeCal = getTimeAsCalendar(deadlineTime);
    Calendar deadline = toCalendar(deadlineDate);
    deadline.set(Calendar.HOUR_OF_DAY, deadlineTimeCal.get(Calendar.HOUR_OF_DAY));
    deadline.set(Calendar.MINUTE, deadlineTimeCal.get(Calendar.MINUTE));
    deadline.set(Calendar.SECOND, deadlineTimeCal.get(Calendar.SECOND));
    return deadline;
  }

  private Calendar toCalendar(String date) {
    Instant.now();
    Calendar calendar = Calendar.getInstance();
    try {
      calendar.setTime(dateFormat.parse(date));
    } catch (ParseException e) {
      Log.e(TAG, "Error while parsing date string", e);
    }
    return calendar;
  }

  private Calendar getTimeAsCalendar(String time) {
    Calendar calendar = Calendar.getInstance();
    try {
      calendar.setTime(timeFormat.parse(time));
    } catch (ParseException e) {
      Log.e(TAG, "Error while parsing date string", e);
    }
    return calendar;
  }
}
