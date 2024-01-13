package com.ttdev;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.INamedParameters;

import java.util.HashMap;
import java.util.Map;

public class MultiAppsDemo extends WebPage {
  public MultiAppsDemo() {
    HashMap<String, Object> state = new HashMap<>();
    state.put("product", "Pen");
    state.put("price", 4);
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state), "<span :style='sty'>{{product}} on sale for {{price}}<span>");
    add(vwa);
    HashMap<String, Object> state2 = new HashMap<>();
    WicketVueApp vwa2=new WicketVueApp("wva2", new Model(state2), "This is another Vue app: <button @click='cb();'>Click me</button>") {
      @Override
      public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {
        state.put("price", (Integer)state.get("price")+1);
        vwa.refresh(target);
      }
    };
    vwa2.setRenderJsScriptElems(false);
    add(vwa2);
  }
}
