package com.foo;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

public class Page4 extends WebPage {
  private final Label lblTotal;
  private int total=0;
  public Page4() {
    lblTotal=new Label("total", new PropertyModel<Integer>(this, "total"));
    lblTotal.setOutputMarkupId(true);
    add(lblTotal);
    add(new JsPostEndPoint("ajaxAddNum") {
      @Override
      protected void respond(AjaxRequestTarget target) {
        total+=getRequest().getRequestParameters().getParameterValue("n").toInt();
        target.add(lblTotal);
      }
    });
  }
}
