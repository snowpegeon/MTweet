package com.github.snowpegeon.mtweet.nico;

import java.time.LocalDateTime;
import java.util.Date;

public class NicoLive {
  private String liveId;

  private String liveUrl;

  private String title;

  private String userName;

  private String userId;

  private String userIcon;

  private String largeThumbnail;

  private LocalDateTime createdAt;

  public String getLiveId() {
    return liveId;
  }

  public void setLiveId(String liveId) {
    this.liveId = liveId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getLargeThumbnail() {
    return largeThumbnail;
  }

  public void setLargeThumbnail(String largeThumbnail) {
    this.largeThumbnail = largeThumbnail;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getLiveUrl() {
    return liveUrl;
  }

  public void setLiveUrl(String liveUrl) {
    this.liveUrl = liveUrl;
  }

  public String getUserIcon() {
    return userIcon;
  }

  public void setUserIcon(String userIcon) {
    this.userIcon = userIcon;
  }
}
