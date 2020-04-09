package com.slate.models.user;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import java.util.Optional;

public class GoogleUser implements SignedInUser {

  private final GoogleSignInAccount account;

  public static Builder builder() {
    return new Builder();
  }

  private GoogleUser(GoogleSignInAccount account) {
    this.account = account;
  }

  @Override
  public String getEmail() {
    return Optional.ofNullable(account)
        .map(GoogleSignInAccount::getEmail)
        .orElse(null);
  }

  public static final class Builder {

    private GoogleSignInAccount account;

    private Builder() {
    }

    public Builder account(GoogleSignInAccount account) {
      this.account = account;
      return this;
    }

    public GoogleUser build() {
      return new GoogleUser(account);
    }
  }
}
