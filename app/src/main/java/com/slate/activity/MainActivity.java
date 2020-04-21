package com.slate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.slate.common.Constants;
import com.slate.dao.TaskDao;
import com.slate.fragments.CalendarFragment;
import com.slate.fragments.CreateTaskFragment;
import com.slate.fragments.HomeScreenFragment;
import com.slate.fragments.RewardsFragment;
import com.slate.fragments.SignInFragment;
import com.slate.models.user.SignedInUser;
import com.slate.service.SchedulingOrchestrator;
import com.slate.service.SlateService;
import com.slate.user.SignInHandler;
import com.slate.user.UserPermission;
import dagger.android.support.DaggerAppCompatActivity;
import java.util.Optional;
import javax.inject.Inject;

public class MainActivity extends DaggerAppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Inject
  SlateService slateService;

  @Inject
  SignInHandler signInHandler;

  @Inject
  SignInFragment signInFragment;

  @Inject
  HomeScreenFragment homeScreenFragment;

  @Inject
  SchedulingOrchestrator schedulingOrchestrator;

  @Inject
  TaskDao taskDao;

  private SignedInUser signedInUser;

  @Inject
  public MainActivity() {
  }

  /**
   * When user clicks on the new task button, shows the new task creation dialog to the user
   *
   * @return nothing
   */
  public View.OnClickListener createTaskButtonListener() {
    return v -> {
      Log.d(TAG, "showCreateTaskDialogCallback!");
      new CreateTaskFragment(schedulingOrchestrator, signedInUser, taskDao)
          .show(getSupportFragmentManager(), "create_task_dialog");
    };
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d("DAGGER", "MainActivity hash: " + hashCode());
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.options_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * Check if a signed in account is already present
   */
  @Override
  protected void onStart() {
    super.onStart();

    //  Request calendar permissions from the user
    UserPermission.requestCalendarPermissions(this);

    Optional<SignedInUser> signedInUserOptional = Optional
        .ofNullable(slateService.checkForLogin(this));

    if (signedInUserOptional.isPresent()) {
      this.signedInUser = signedInUserOptional.get();
      showHomeScreenFragment();
    } else {
      showSignInFragment();
    }

    ((BottomNavigationView) findViewById(R.id.bottom_nav_bar))
        .setOnNavigationItemSelectedListener(getBottomTabSelectionListener());
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
    if (requestCode == Constants.IntentRC.SIGN_IN) {
      signedInUser = slateService.handleSignIn(data, this);
      showHomeScreenFragment();
    }
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.sign_out_button:
        Toast.makeText(this, "Sign Out selected", Toast.LENGTH_SHORT).show();
        signInHandler.handleSignOut(this);
        showSignInFragment();
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private BottomNavigationView.OnNavigationItemSelectedListener getBottomTabSelectionListener() {
    return item -> {
      Fragment selectedFragment = null;

      switch (item.getItemId()) {
        case R.id.nav_tasks:
          selectedFragment = homeScreenFragment;
          break;

        case R.id.nav_calendar:
          selectedFragment = new CalendarFragment();
          break;

        case R.id.nav_rewards:
          selectedFragment = new RewardsFragment();
          break;
      }
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.main_container, selectedFragment).commit();

      return true;
    };
  }

  private void showHomeScreenFragment() {
    getSupportFragmentManager().popBackStack(SignInFragment.class.getSimpleName(),
        FragmentManager.POP_BACK_STACK_INCLUSIVE);
    replaceFragment(R.id.main_container, homeScreenFragment, true);
  }

  private void showSignInFragment() {
    getSupportFragmentManager().popBackStackImmediate();
    replaceFragment(R.id.main_container, signInFragment, true);
  }

  private void replaceFragment(int oldFragmentId, Fragment newFragment, boolean addToBackStack) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(oldFragmentId, newFragment);
    if (addToBackStack) {
      transaction.addToBackStack(newFragment.getClass().getSimpleName());
    }
    transaction.commit();
  }
}
