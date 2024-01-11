package com.ttdev;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import java.util.HashMap;
import java.util.Map;

public class RouteDemo extends WebPage {

  private final HashMap<String, Object> state;
  private Label msg;

  public RouteDemo() {
    state = new HashMap<>();
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state),
        "<button @click='cb(\"onB1Click\")'>OK</button><button @click='cb(\"onB2Click\")'>Cancel</button>") {
      public void onB1Click(AjaxRequestTarget target, Map<String, Object> data) {
        msg.setDefaultModelObject("B1 clicked");
        target.add(msg);
      }
      public void onB2Click(AjaxRequestTarget target, Map<String, Object> data) {
        msg.setDefaultModelObject("B2 clicked");
        target.add(msg);
      }
    };
    add(vwa);
    msg=new Label("msg", new Model<String>());
    msg.setOutputMarkupId(true);
    add(msg);
  }
}
