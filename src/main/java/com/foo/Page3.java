package com.foo;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;

public class Page3 extends WebPage {
  private int length=5;
  public Page3() {
    PropertyModel<Integer> m1=new PropertyModel<>("Hello", "length");
    PropertyModel<Integer> m2=new PropertyModel<>(this, "length");
    m1.getObject();
    m2.getObject();

  }
}
