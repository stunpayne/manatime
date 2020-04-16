package com.slate.service;

public interface SharedPrefManager {

  String getSignedInUserEmail();

  void setSignedInUserEmail(String email);
}
