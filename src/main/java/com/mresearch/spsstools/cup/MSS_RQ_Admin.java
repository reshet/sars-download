package com.mresearch.spsstools.cup;

public class MSS_RQ_Admin
    implements MSS_RQ_Face_Admin, Service_Invokation_Handler
{
  private String ME;
  private String userID;
  private String pswd;
  private String prepRQ;
  private String endofRQ;
  private String endofRQ_base;
  private String endofRQ2;
  private StringBuilder bulk_of_RQs;
  private int bulkSize = 0;
  private boolean bulk_mode = false;

  public MSS_RQ_Admin(String me, String user, String pswd)
  {
    this.ME = me;
    this.userID = user;
    this.pswd = pswd;
    upprepRQ();
  }

  public void upprepRQ()
  {
    this.prepRQ = ("<RQ SERVICE = \"MSS\" ME = \"" + this.ME + "\" SCREEN = \"240\" USERID = \"" + this.userID + "\">");

    this.endofRQ = (" USERID = \"" + this.userID + "\" PSWD = \"" + getPswd() + "\"/></RQ>");

    this.endofRQ_base = (" USERID = \"" + this.userID + "\" PSWD = \"" + getPswd() + "\"/>");
    this.endofRQ2 = "</RQ>";
  }

  public String processBulk()
  {
    String preporRQ = "<RQ SERVICE = \"MSS\" ME = \"" + this.ME + "\" SCREEN = \"240\" USERID = \"" + this.userID + "\" M_MODE=\"ENABLED\">";
    if ((this.bulk_of_RQs != null) || (this.bulk_mode == true))
    {
      this.bulk_mode = false;
      return preporRQ + this.bulk_of_RQs.toString() + this.endofRQ2;
    }
    return "";
  }

  public void enableBulk()
  {
    this.bulk_of_RQs = new StringBuilder();
    this.bulkSize = 0;
    this.bulk_mode = true;
  }

  public int bulkSize()
  {
    return this.bulkSize;
  }

  public void disableBulk()
  {
    this.bulk_mode = false;
    this.bulkSize = 0;
  }

  private String compactWrapper(String method)
  {
    if (this.bulk_mode)
    {
      this.bulk_of_RQs.append(method);
      this.bulkSize += 1;
    }
    return this.prepRQ + method + this.endofRQ2;
  }

  public String createTask(String task, String desc, int scope, int pattern)
  {
    return this.prepRQ + "<method name = \"createTask\" TASK=\"" + task + "\" DESC=\"" + desc + "\" SCOPE=\"" + String.valueOf(scope) + "\" PATTERN=\"" + String.valueOf(pattern) + "\"" + this.endofRQ;
  }

  public String deleteTask(int n)
  {
    return this.prepRQ + "<method name = \"deleteTask\" TASK=\"" + String.valueOf(n) + "\"" + this.endofRQ;
  }

  public String updateTask(String task, String desc, int n)
  {
    return this.prepRQ + "<method name = \"updateTask\" TASK=\"" + task + "\" DESC=\"" + desc + "\" N=\"" + String.valueOf(n) + "\"" + this.endofRQ;
  }

  public String openTask(int n)
  {
    String method = "<method name = \"openTask\" TASK=\"" + String.valueOf(n) + "\"" + this.endofRQ_base;
    if (this.bulk_mode)
    {
      this.bulk_of_RQs.append(method);
      this.bulkSize += 1;
    }
    return this.prepRQ + method + this.endofRQ2;
  }

  public String closeTask(int n)
  {
    return this.prepRQ + "<method name = \"closeTask\" TASK=\"" + String.valueOf(n) + "\"" + this.endofRQ;
  }

  public String shareTask(int n)
  {
    String method = "<method name = \"shareTask\" TASK=\"" + String.valueOf(n) + "\"" + this.endofRQ_base;
    if (this.bulk_mode)
    {
      this.bulk_of_RQs.append(method);this.bulkSize += 1;
    }
    return this.prepRQ + method + this.endofRQ2;
  }

  public String hideTask(int n)
  {
    return this.prepRQ + "<method name = \"hideTask\" TASK=\"" + String.valueOf(n) + "\"" + this.endofRQ;
  }

  public String unfoldTask(int n)
  {
    return this.prepRQ + "<method name = \"unfoldTask\" TASK=\"" + String.valueOf(n) + "\"" + this.endofRQ;
  }

  public String makeGroup(String NAME, String Descr)
  {
    return this.prepRQ + "<method name = \"makeGroup\" NAME=\"" + NAME + "\" DESC=\"" + Descr + "\"" + this.endofRQ;
  }

  public String makeGroup(int id, String NAME, String Descr)
  {
    return this.prepRQ + "<method name = \"makeGroup\" NAME=\"" + NAME + "\" DESC=\"" + Descr + "\" ID=\"" + String.valueOf(id) + "\"" + this.endofRQ;
  }

  public String makeGroup(int id, int for_user, String NAME, String Descr)
  {
    return this.prepRQ + "<method name = \"makeGroup\" NAME=\"" + NAME + "\" DESC=\"" + Descr + "\" ID=\"" + String.valueOf(id) + "\" FORUSER=\"" + String.valueOf(for_user) + "\"" + this.endofRQ;
  }

  public String editGroup(int n, String NAME)
  {
    return this.prepRQ + "<method name = \"editGroup\" ID=\"" + String.valueOf(n) + "\" NAME=\"" + NAME + "\"" + this.endofRQ;
  }

  public String deleteGroup(int n)
  {
    return this.prepRQ + "<method name = \"deleteGroup\" N=\"" + String.valueOf(n) + "\"" + this.endofRQ;
  }

  public String listOwnGroups()
  {
    return this.prepRQ + "<method name = \"listOwnGroups\"" + this.endofRQ;
  }

  public String listGroupMembers(int n)
  {
    return this.prepRQ + "<method name = \"listGroupMembers\" GROUP = \"" + String.valueOf(n) + "\"" + this.endofRQ;
  }

  public String listTasksOwn()
  {
    return this.prepRQ + "<method name = \"listTasksOwn\"" + this.endofRQ;
  }

  public String listTasksOwnW(int group)
  {
    return this.prepRQ + "<method name = \"listTasksOwnW\" group=\"" + String.valueOf(group) + "\"" + this.endofRQ;
  }

  public String geatherStatistics(int n)
  {
    String method = "<method name = \"geatherStatistics\" TASK=\"" + String.valueOf(n) + "\"/>";
    if (this.bulk_mode)
    {
      this.bulk_of_RQs.append(method);this.bulkSize += 1;
    }
    return this.prepRQ + method + this.endofRQ2;
  }

  public String representStatistics(int n)
  {
    return this.prepRQ + "<method name = \"representStatistics\" TASK=\"" + String.valueOf(n) + "\"" + this.endofRQ;
  }

  public String representStatistics(int n, int by_user_id)
  {
    return this.prepRQ + "<method name = \"representStatistics\" TASK=\"" + String.valueOf(n) + "\" BY_USER_ID=\"" + String.valueOf(by_user_id) + this.endofRQ;
  }

  public String representAllStatistics()
  {
    return this.prepRQ + "<method name = \"representAllStatistics\"" + this.endofRQ;
  }

  public String registerUser(int id, int group, String LOGIN, String pswd)
  {
    String method = "<method name = \"registerUser\" CORP=\"2\" GROUP=\"" + String.valueOf(group) + "\" USERID=\"" + String.valueOf(id) + "\" LOGIN=\"" + String.valueOf(LOGIN) + "\" PSWD=\"" + String.valueOf(pswd) + "\"/>";
    if (this.bulk_mode)
    {
      this.bulk_of_RQs.append(method);this.bulkSize += 1;
    }
    return this.prepRQ + method + this.endofRQ2;
  }

  public String registerUser(int group, String LOGIN, String pswd)
  {
    String method = "<method name = \"registerUser\" CORP=\"2\" GROUP=\"" + String.valueOf(group) + "\" LOGIN=\"" + String.valueOf(LOGIN) + "\" PSWD=\"" + String.valueOf(pswd) + "\"/>";
    if (this.bulk_mode)
    {
      this.bulk_of_RQs.append(method);this.bulkSize += 1;
    }
    return this.prepRQ + method + this.endofRQ2;
  }

  public String enrollToGroup(int idUser, int idGroup, String pswd)
  {
    return this.prepRQ + "<method name = \"enrollToGroup\"  GROUP=\"" + String.valueOf(idGroup) + "\" USERID=\"" + String.valueOf(idUser) + "\" PSWD=\"" + String.valueOf(pswd) + "\"/>" + "</RQ>";
  }

  public String kickUserFromGroup(int idUser, int idGroup)
  {
    return this.prepRQ + "<method name = \"kickUserFromGroup\"  GROUP=\"" + String.valueOf(idGroup) + "\" USER=\"" + String.valueOf(idUser) + "\" " + this.endofRQ;
  }

  public String listHeirGroups(int n)
  {
    return this.prepRQ + "<method name = \"listHeirGroups\" GROUP = \"" + String.valueOf(n) + "\"" + this.endofRQ;
  }

  public String addHeirGroup(int superID, int heirID)
  {
    return this.prepRQ + "<method name = \"bindHeirGroup\" ID = \"" + String.valueOf(superID) + "\"" + " ID_HEIR = \"" + String.valueOf(heirID) + "\"" + this.endofRQ;
  }

  public String sitTask(int taskID, String answer)
  {
    return this.prepRQ + "<method name = \"sitTask\" TASK=\"" + String.valueOf(taskID) + "\" ANSWER=\"" + answer + "\"" + this.endofRQ;
  }

  public String extendScopeTask(int taskID, int groupID)
  {
    String method = "<method name = \"extendScopeTask\" TASK=\"" + String.valueOf(taskID) + "\" GROUP=\"" + String.valueOf(groupID) + "\" " + this.endofRQ_base;
    if (this.bulk_mode)
    {
      this.bulk_of_RQs.append(method);this.bulkSize += 1;
    }
    return this.prepRQ + method + this.endofRQ2;
  }

  public String descendScopeTask(int taskID, int groupID)
  {
    String method = "<method name = \"descendScopeTask\" TASK=\"" + String.valueOf(taskID) + "\" GROUP=\"" + String.valueOf(groupID) + "\" " + this.endofRQ_base;
    if (this.bulk_mode)
    {
      this.bulk_of_RQs.append(method);this.bulkSize += 1;
    }
    return this.prepRQ + method + this.endofRQ2;
  }

  public String listTasksAviable()
  {
    return this.prepRQ + "<method name = \"listTasksAviable\"" + this.endofRQ;
  }

  public String listTasksAviable2(int gr)
  {
    return this.prepRQ + "<method name = \"listTasksAviable\"" + this.endofRQ;
  }

  public String force_subscribeTask(int task, int user)
  {
    String method = "<method name = \"force_subscribeTask\" TASK=\"" + String.valueOf(task) + "\" SUBSCRIBER_USER=\"" + String.valueOf(user) + "\" " + this.endofRQ_base;
    if (this.bulk_mode)
    {
      this.bulk_of_RQs.append(method);this.bulkSize += 1;
    }
    return this.prepRQ + method + this.endofRQ2;
  }

  public String force_unsubscribeTask(int task, int user)
  {
    String method = "<method name = \"force_unsubscribeTask\" TASK=\"" + String.valueOf(task) + "\" UNSUBSCRIBER_USER=\"" + String.valueOf(user) + "\" " + this.endofRQ_base;
    if (this.bulk_mode)
    {
      this.bulk_of_RQs.append(method);this.bulkSize += 1;
    }
    return this.prepRQ + method + this.endofRQ2;
  }

  public String getME()
  {
    return this.ME;
  }

  public void setME(String ME)
  {
    this.ME = ME;
    upprepRQ();
  }

  public String getUserID()
  {
    return this.userID;
  }

  public void setUserID(String userID)
  {
    this.userID = userID;
    upprepRQ();
  }

  public String getPswd()
  {
    return this.pswd;
  }

  public void setPswd(String pswd)
  {
    this.pswd = pswd;
    upprepRQ();
  }

  public String listGroupMembersDetailed(int group, int idRegIv)
  {
    String method = "<method name = \"listGroupMembersDetailed\" GROUP=\"" + String.valueOf(group) + "\" ID_REG_IV=\"" + String.valueOf(idRegIv) + "\"" + this.endofRQ_base;

    return compactWrapper(method);
  }

  public String getTasksDoneCount(int task)
  {
    String method = "<method name = \"getTasksDoneCount\" TASK=\"" + String.valueOf(task) + "\" " + this.endofRQ_base;

    return compactWrapper(method);
  }
}
