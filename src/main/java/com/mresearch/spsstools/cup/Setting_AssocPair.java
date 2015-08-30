package com.mresearch.spsstools.cup;

public class Setting_AssocPair<LeftType, RightType>
{
  private LeftType left;
  private RightType right;

  public Setting_AssocPair(LeftType lft, RightType rht)
  {
    this.left = lft;
    this.right = rht;
  }

  public LeftType getLeft()
  {
    return (LeftType)this.left;
  }

  public RightType getRight()
  {
    return (RightType)this.right;
  }

  public void setLeft(LeftType left)
  {
    this.left = left;
  }

  public void setRight(RightType right)
  {
    this.right = right;
  }
}
