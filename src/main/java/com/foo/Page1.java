package com.foo;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

public class Page1 extends WebPage {
  private final Label lbl;
  private int n = 0;

  public Page1() {
    IModel<String> m1 = new LoadableDetachableModel<String>() {

      @Override
      protected String load() {
        return "";
      }
    };
    lbl = new Label("lbl", m1);
    lbl.setOutputMarkupId(true);
    add(lbl);
    add(new JsPostEndPoint("ajaxSetPrice") {
      @Override
      protected void respond(AjaxRequestTarget target) {
        n = getRequest().getRequestParameters().getParameterValue("n").toInt();
        target.add(lbl);
      }
    });
  }

}
