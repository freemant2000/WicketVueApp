package com.foo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

import java.util.HashMap;
import java.util.Map;

public abstract class WicketVueAppJs extends Label {
  private final AbstractDefaultAjaxBehavior ajaxBe;
  private final Map<String, Object> state;
  private final String appDivId;
  private final String vueTemplate;
  private final String vueMethods;

  public WicketVueAppJs(String id, String appDivId, Map<String, Object> state, String vueTemplate, String vueMethods) {
    super(id);
    this.state=state;
    this.appDivId=appDivId;
    this.vueTemplate=vueTemplate;
    this.vueMethods=vueMethods;
    ajaxBe = new AbstractDefaultAjaxBehavior() {
      @Override
      protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        attributes.getDynamicExtraParameters().add("return {p1: evJson};");
        attributes.setMethod(AjaxRequestAttributes.Method.POST);
      }

      @Override
      protected void respond(AjaxRequestTarget target) {
        String json = RequestCycle.get().getRequest().getRequestParameters().getParameterValue("p1").toString();
        Map<String, Object> data=getDataFromJson(json);
        Map<String, Object>  newState= (Map<String, Object>) data.remove("state");
        onVueEvent(target, newState, data);
        target.appendJavaScript(String.format("Object.assign(document.getElementById('%s').myVueInst.$data, %s)", appDivId, getDataAsJsObj()));
      }

      private Map<String, Object> getDataFromJson(String json) {
        ObjectMapper om=new ObjectMapper();
        try {
          Map<String, Object> m=om.readValue(json, Map.class);
          return m;
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      }

    };
    add(ajaxBe);
  }
  public abstract void onVueEvent(AjaxRequestTarget target, Map<String, Object> newState, Map<String, Object> data);

  private String getDataAsJsObj() {
    JsObjectStrFormatter f=new JsObjectStrFormatter();
    String js=f.formatAsJSObject(state);
    return js;
  }

  @Override
  public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
    TextTemplate tt =new PackageTextTemplate(this.getClass(), "WicketVueAppJs.txt");
    Map<String, String> bindings=new HashMap<>();
    JsObjectStrFormatter f=new JsObjectStrFormatter();
    bindings.put("initData", f.formatAsJSObject(state));
    bindings.put("ajaxCall", ajaxBe.getCallbackFunctionBody().toString());
    bindings.put("appDivId", appDivId);
    bindings.put("vueTemplate", vueTemplate);
    bindings.put("vueMethods", vueMethods);
    String jsCode = tt.asString(bindings);
    replaceComponentTagBody(markupStream, openTag, jsCode);
  }

  @Override
  public void renderHead(IHeaderResponse response) {
    super.renderHead(response);
    response.render(JavaScriptHeaderItem.forUrl("https://cdn.jsdelivr.net/npm/vue@3/dist/vue.global.js", "vue3"));
  }
}


