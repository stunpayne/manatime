package com.slate.models.calendar;

public class Calendar {

  private String id;
  private String displayName;
  private String accountName;
  private String ownerAccount;
  private Boolean primary;
  private Boolean visible;
  private String timeZone;

  public static CalendarBuilder builder() {
    return new CalendarBuilder();
  }

  public String getId() {
    return id;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getAccountName() {
    return accountName;
  }

  public String getOwnerAccount() {
    return ownerAccount;
  }

  public Boolean getPrimary() {
    return primary;
  }

  public Boolean getVisible() {
    return visible;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public static final class CalendarBuilder {

    private String id;
    private String displayName;
    private String accountName;
    private String ownerAccount;
    private Boolean primary;
    private Boolean visible;
    private String timeZone;

    private CalendarBuilder() {
    }

    public CalendarBuilder id(String id) {
      this.id = id;
      return this;
    }

    public CalendarBuilder displayName(String displayName) {
      this.displayName = displayName;
      return this;
    }

    public CalendarBuilder accountName(String accountName) {
      this.accountName = accountName;
      return this;
    }

    public CalendarBuilder ownerAccount(String ownerAccount) {
      this.ownerAccount = ownerAccount;
      return this;
    }

    public CalendarBuilder primary(Boolean primary) {
      this.primary = primary;
      return this;
    }

    public CalendarBuilder visible(Boolean visible) {
      this.visible = visible;
      return this;
    }

    public CalendarBuilder timeZone(String timeZone) {
      this.timeZone = timeZone;
      return this;
    }

    public Calendar build() {
      Calendar calendar = new Calendar();
      calendar.id = this.id;
      calendar.primary = this.primary;
      calendar.accountName = this.accountName;
      calendar.ownerAccount = this.ownerAccount;
      calendar.displayName = this.displayName;
      calendar.visible = this.visible;
      calendar.timeZone = this.timeZone;
      return calendar;
    }
  }

  @Override
  public String toString() {
    return "{\"id\" : " + (id == null ? null : "\"" + id + "\"") + ",\"displayName\" : " + (
        displayName == null ? null : "\"" + displayName + "\"") + ",\"accountName\" : " + (
        accountName == null ? null : "\"" + accountName + "\"") + ",\"ownerAccount\" : " + (
        ownerAccount == null ? null : "\"" + ownerAccount + "\"") + ",\"primary\" : " + primary
        + ",\"visible\" : " + visible + ",\"timeZone\" : " + (timeZone == null ? null
        : "\"" + timeZone + "\"") + "}";
  }

}
