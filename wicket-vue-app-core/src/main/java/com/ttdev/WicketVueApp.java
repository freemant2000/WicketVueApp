package com.ttdev;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

import java.lang.reflect.Method;
import java.util.Map;

public class WicketVueApp extends Panel {
  private Map<String, Object> state;

  public WicketVueApp(String id, Map<String, Object> state, String vueTemplate) {
    this(id, state, vueTemplate, "");
  }
  public WicketVueApp(String id, Map<String, Object> state, String vueTemplate, String vueMethods) {
    super(id);
    this.state = state;
    WebMarkupContainer appDiv = new WebMarkupContainer("appDiv");
    appDiv.setOutputMarkupId(true);
    add(appDiv);
    add(new WicketVueAppJs("jsCode", appDiv.getMarkupId(), state, vueTemplate, vueMethods) {

      @Override
      public void onVueEvent(AjaxRequestTarget target, Map<String, Object> newState, Map<String, Object> data) {
        state.putAll(newState);
        String route=(String)data.remove("route");
        if (route.isEmpty()) {
          WicketVueApp.this.onVueEvent(target, data);
        } else {
          try {
            Method m = WicketVueApp.this.getClass().getMethod(route, AjaxRequestTarget.class, Map.class);
            m.invoke(WicketVueApp.this, target, data);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      }
    });
  }
  public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {

  }
}
