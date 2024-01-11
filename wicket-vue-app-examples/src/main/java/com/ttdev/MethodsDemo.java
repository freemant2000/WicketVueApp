package com.ttdev;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

import java.util.HashMap;

public class MethodsDemo extends WebPage {
  public MethodsDemo() {
    HashMap<String, Object> state = new HashMap<>();
    state.put("n", 0);
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state), "<span @click='m1'>Click me: {{n}}<span>", "m1() { this.n++; }");
    add(vwa);
  }
}
