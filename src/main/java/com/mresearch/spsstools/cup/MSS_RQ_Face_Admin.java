package com.mresearch.spsstools.cup;

public abstract interface MSS_RQ_Face_Admin
{
  public abstract String createTask(String paramString1, String paramString2, int paramInt1, int paramInt2);

  public abstract String deleteTask(int paramInt);

  public abstract String updateTask(String paramString1, String paramString2, int paramInt);

  public abstract String openTask(int paramInt);

  public abstract String closeTask(int paramInt);

  public abstract String shareTask(int paramInt);

  public abstract String hideTask(int paramInt);

  public abstract String unfoldTask(int paramInt);

  public abstract String makeGroup(String paramString1, String paramString2);

  public abstract String makeGroup(int paramInt, String paramString1, String paramString2);

  public abstract String makeGroup(int paramInt1, int paramInt2, String paramString1, String paramString2);

  public abstract String editGroup(int paramInt, String paramString);

  public abstract String deleteGroup(int paramInt);

  public abstract String listOwnGroups();

  public abstract String listTasksOwn();

  public abstract String listTasksOwnW(int paramInt);

  public abstract String listGroupMembers(int paramInt);

  public abstract String listGroupMembersDetailed(int paramInt1, int paramInt2);

  public abstract String geatherStatistics(int paramInt);

  public abstract String representStatistics(int paramInt);

  public abstract String representAllStatistics();

  public abstract String registerUser(int paramInt1, int paramInt2, String paramString1, String paramString2);

  public abstract String registerUser(int paramInt, String paramString1, String paramString2);

  public abstract String enrollToGroup(int paramInt1, int paramInt2, String paramString);

  public abstract String kickUserFromGroup(int paramInt1, int paramInt2);

  public abstract String listHeirGroups(int paramInt);

  public abstract String addHeirGroup(int paramInt1, int paramInt2);

  public abstract String sitTask(int paramInt, String paramString);

  public abstract String extendScopeTask(int paramInt1, int paramInt2);

  public abstract String descendScopeTask(int paramInt1, int paramInt2);

  public abstract String listTasksAviable();

  public abstract String listTasksAviable2(int paramInt);

  public abstract String force_subscribeTask(int paramInt1, int paramInt2);

  public abstract String force_unsubscribeTask(int paramInt1, int paramInt2);

  public abstract String getTasksDoneCount(int paramInt);
}
