package com.mresearch.spsstools.cup;

public class MSS_RQ_TableDescriptor
{
  private String[] descriptor;
  private Class[] class_descriptor;

  public MSS_RQ_TableDescriptor(String[] desc, Class[] class_desc)
  {
    this.descriptor = desc;
    this.class_descriptor = class_desc;
  }

  public String[] getDescription()
  {
    return this.descriptor;
  }

  public Class[] getClassDescription()
  {
    return this.class_descriptor;
  }
}
