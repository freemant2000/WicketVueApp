package com.ttdev;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServerSideEventHandlingDemo extends WebPage {

  private final HashMap<String, Object> state;
  private Label lastUpd;
  public ServerSideEventHandlingDemo() {
    state = new HashMap<>();
    state.put("n", 0);
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state),
        "<span @click='cb()'>Click me: {{n}}<span>") {
      @Override
      public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {
        state.put("n", (Integer)state.get("n")+1);
        target.add(lastUpd);
      }
    };
    add(vwa);
    lastUpd=new Label("lastUpd", LambdaModel.of(()->new Date().toString()));
    lastUpd.setOutputMarkupId(true);
    add(lastUpd);
  }
}
