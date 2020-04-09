package com.slate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.slate.activity.R;
import com.slate.user.SignInHandler;
import dagger.android.support.DaggerFragment;
import java.util.Optional;
import javax.inject.Inject;

public class SignInFragment extends DaggerFragment {

  private static final String TAG = SignInFragment.class.getSimpleName();

  private final SignInHandler signInHandler;

  @Inject
  public SignInFragment(SignInHandler signInHandler) {
    this.signInHandler = signInHandler;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    Toast.makeText(getActivity(), "Sign In Fragment", Toast.LENGTH_SHORT).show();
    return inflater.inflate(R.layout.signin_fragment, container, false);
  }

  @Override
  public void onStart() {
    super.onStart();
    setupGoogleSignIn();
  }

  private void setupGoogleSignIn() {
    Optional.ofNullable(getActivity().findViewById(R.id.sign_in_button))
        .ifPresent(view -> ((View) view)
            .setOnClickListener(signInHandler.createSignInButtonListener(getActivity())));
  }
}
