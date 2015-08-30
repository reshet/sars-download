package com.mresearch.spsstools.cup;

public class MSS_Pair
{
  private String name;
  private String value;

  public MSS_Pair(String name, String value)
  {
    this.name = name;
    this.value = value;
  }

  public String getName()
  {
    return this.name;
  }

  public String getValue()
  {
    return this.value;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setValue(String value)
  {
    this.value = value;
  }
}
