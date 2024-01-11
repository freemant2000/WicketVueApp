package com.ttdev;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

import java.util.HashMap;

public class InitialDataDemo extends WebPage {
  public InitialDataDemo() {
    HashMap<String, Object> state = new HashMap<String, Object>();
    state.put("product", "Pen");
    state.put("price", 4);
    HashMap<String, Object> styMap = new HashMap<String, Object>();
    styMap.put("backgroundColor", "green");
    styMap.put("fontSize", "18");
    state.put("sty", styMap);
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state), "<span :style='sty'>{{product}} on sale for {{price}}<span>");
    add(vwa);
  }
}
