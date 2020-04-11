package com.slate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.slate.common.Constants;
import com.slate.fragments.CreateTaskFragment;
import com.slate.fragments.HomeScreenFragment;
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
      new CreateTaskFragment(schedulingOrchestrator, signedInUser)
          .show(getSupportFragmentManager(), "create_task_dialog");
    };
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d("DAGGER", "MainActivity hash: " + hashCode());
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  /**
   * Check if a signed in account is already present
   */
  @Override
  protected void onStart() {
    super.onStart();

    //  Request calendar permissions from the user
    UserPermission.requestCalendarPermissions(this);

    Optional<GoogleSignInAccount> googleSignInAccount = Optional
        .ofNullable(slateService.checkForLogin(this));
    if (googleSignInAccount.isPresent()) {
      //  Disable sign in button
      //  Prepare current user's calendar
      //  TODO: Remove fragment from here
      showSignInFragment();
    } else {
      showSignInFragment();
    }
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

  private void showHomeScreenFragment() {
    getSupportFragmentManager().popBackStack();
    replaceFragment(R.id.main_container, homeScreenFragment, true);
  }

  private void showSignInFragment() {
    replaceFragment(R.id.main_container, signInFragment, true);
  }

  private void replaceFragment(int oldFragmentId, Fragment newFragment, boolean addToBackStack) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(oldFragmentId, newFragment);
    if (addToBackStack) {
      transaction.addToBackStack(null);
    }
    transaction.commit();
  }
}
