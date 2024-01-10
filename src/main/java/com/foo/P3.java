package com.foo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

import java.util.HashMap;
import java.util.Map;

public class P3 extends WebPage {
  private final Map<String, Object> state;
  private Label normalLbl;
  private String msg="OK";

  public P3() {
    state = new HashMap<String, Object>();
    state.put("a", 400);
    state.put("b", "Hello");
    WicketVueApp vwa=new WicketVueApp("wva", state, "<b @click='cb({x: \"Good\"})'>{{a}}</b><input v-model='b'>") {
      @Override
      public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {
        state.put("a", (Integer)state.get("a")+2);
        msg="default handler";
        target.add(normalLbl);
      }
      public void abc(AjaxRequestTarget target, Map<String, Object> data) {
        state.put("a", (Integer)state.get("a")+1);
        msg=(String)data.get("x")+state.get("b");
        target.add(normalLbl);
      }
    };
    add(vwa);
    normalLbl=new Label("lbl", new PropertyModel<>(this, "msg"));
    normalLbl.setOutputMarkupId(true);
    add(normalLbl);

  }
}
