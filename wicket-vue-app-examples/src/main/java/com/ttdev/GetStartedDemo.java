package com.ttdev;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

import java.util.HashMap;
import java.util.Map;

public class GetStartedDemo extends WebPage {
  public GetStartedDemo() {
    HashMap<String, Object> state = new HashMap<>();
    state.put("a", 4);
    state.put("b", "Hi");
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state), "<span @click='cb()'>{{a}} {{b}}<span>") {
      @Override
      public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {
        state.put("b", state.get("b")+"!");
      }
    };
    add(vwa);
  }
}
