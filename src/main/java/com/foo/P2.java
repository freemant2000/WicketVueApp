package com.foo;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class P2 extends WebPage {
  private int a;
  private int b;
  public P2() {
    a=2;
    b=6;
    IModel<Integer> m=new Model<>() {
      @Override
      public Integer getObject() {
        return a+b;
      }
    };
    a=4;
    System.out.println(m.getObject());
  }
}
