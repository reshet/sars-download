package com.mresearch.spsstools.cup;

import java.util.ArrayList;

public class MSS_RQ_XML_Pattern
{
  private ArrayList<MSS_Pair> attributes;
  private String nodename;

  public MSS_RQ_XML_Pattern()
  {
    this.attributes = new ArrayList(20);
  }

  public ArrayList<MSS_Pair> getAttributes()
  {
    return this.attributes;
  }

  public void setAttributes(ArrayList<MSS_Pair> attributes)
  {
    this.attributes = attributes;
  }

  public String getNodename()
  {
    return this.nodename;
  }

  public void setNodename(String nodename)
  {
    this.nodename = nodename;
  }
}
