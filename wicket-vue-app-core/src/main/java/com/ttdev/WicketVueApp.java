package com.ttdev;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class WicketVueApp extends Panel {
  public static final String PARAM_NAME_EVENT_DATA = "eventData";
  private IModel<? extends Map<String, Object>> state;
  private final AbstractDefaultAjaxBehavior ajaxBe;
  private final String appDivId;
  private final String vueTemplate;
  private final String vueMethods;
  private String vueUrl="https://cdn.jsdelivr.net/npm/vue@3/dist/vue.esm-browser.prod.js";

  public WicketVueApp(String id, IModel<? extends  Map<String, Object>> state, String vueTemplate) {
    this(id, state, vueTemplate, "");
  }

  public WicketVueApp(String id, IModel<? extends Map<String, Object>> state, String vueTemplate, String vueMethods) {
    super(id);
    this.state = state;
    this.vueTemplate = vueTemplate;
    this.vueMethods = vueMethods;
    ajaxBe = new AbstractDefaultAjaxBehavior() {
      @Override
      protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        attributes.getDynamicExtraParameters().add("return {eventData: evJson};");
        attributes.setMethod(AjaxRequestAttributes.Method.POST);
      }

      @Override
      protected void respond(AjaxRequestTarget target) {
        String json = RequestCycle.get().getRequest().getRequestParameters().getParameterValue(PARAM_NAME_EVENT_DATA).toString();
        Map<String, Object> data = getDataFromJson(json);
        Map<String, Object> newState = (Map<String, Object>) data.remove("state");
        state.getObject().putAll(newState);
        String route = (String) data.remove("route");
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
        target.appendJavaScript(String.format("Object.assign(document.getElementById('%s').myVueInst.$data, %s)", appDivId, getStateAsJsObj()));
      }
    };
    add(ajaxBe);
    WebMarkupContainer appDiv = new WebMarkupContainer("appDiv");
    appDiv.setOutputMarkupId(true);
    appDivId = appDiv.getMarkupId();
    add(appDiv);
    Label jsLbl =new Label("jsCode", new PropertyModel<String>(this, "jsCode"));
    jsLbl.setEscapeModelStrings(false);
    add(jsLbl);
  }

  public void setVueUrl(String vueUrl) {
    this.vueUrl = vueUrl;
  }

  private String getStateAsJsObj() {
    ObjectMapper om = new ObjectMapper();
    try {
      String json = om.writeValueAsString(state.getObject());
      return json;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  private Map<String, Object> getDataFromJson(String json) {
    ObjectMapper om = new ObjectMapper();
    try {
      Map<String, Object> m = om.readValue(json, Map.class);
      return m;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
    public String getJsCode() {
    TextTemplate tt = new PackageTextTemplate(this.getClass(), "WicketVueApp.js");
    Map<String, String> bindings = new HashMap<>();
    bindings.put("vueUrl", vueUrl);
    bindings.put("initData", getStateAsJsObj());
    bindings.put("ajaxCall", ajaxBe.getCallbackFunctionBody().toString());
    bindings.put("appDivId", appDivId);
    bindings.put("vueTemplate", vueTemplate);
    bindings.put("vueMethods", vueMethods);
    String jsCode = tt.asString(bindings);
    return jsCode;
  }


  public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {

  }
}
