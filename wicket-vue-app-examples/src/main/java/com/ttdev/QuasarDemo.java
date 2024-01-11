package com.ttdev;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import java.util.HashMap;
import java.util.Map;

public class QuasarDemo extends WebPage {

  private final HashMap<String, Object> state;


  public QuasarDemo() {
    state = new HashMap<>();
    state.put("n", 0);
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state),
        "<q-btn @click='cb()'>{{n}}</q-btn>") {
      @Override
      public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {
        state.put("n", (Integer)state.get("n")+1);
      }
    };
    vwa.useQuasar();
    add(vwa);
  }
}
