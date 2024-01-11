package com.ttdev;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import java.util.HashMap;
import java.util.Map;

public class RefreshByWicketDemo extends WebPage {
  private final HashMap<String, Object> state;

  public RefreshByWicketDemo() {
    state = new HashMap<>();
    state.put("n", 0);
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state),"<span>{{n}}</span>");
    add(vwa);
    AjaxLink ok=new AjaxLink<Void>("ok") {

      @Override
      public void onClick(AjaxRequestTarget target) {
        state.put("n", (Integer)state.get("n")+1);
        vwa.refresh(target);
      }
    };
    add(ok);
  }
}
