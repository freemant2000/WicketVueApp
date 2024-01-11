package com.ttdev;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import java.util.HashMap;
import java.util.Map;

public class VueModelDemo extends WebPage {

  private final HashMap<String, Object> state;
  private Label msg;

  public VueModelDemo() {
    state = new HashMap<>();
    state.put("v", "Hi");
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state),
        "<input v-model='v'><button @click='cb()'>OK</button>") {
      @Override
      public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {
        msg.setDefaultModelObject("Got: "+state.get("v"));
        target.add(msg);
      }
    };
    add(vwa);
    msg=new Label("msg", new Model<String>());
    msg.setOutputMarkupId(true);
    add(msg);
  }
}
