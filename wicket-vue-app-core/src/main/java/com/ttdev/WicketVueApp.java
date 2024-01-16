package com.ttdev;

/*-
 * #%L
 * wicket-vue-app-core
 * %%
 * Copyright (C) 2024 TipTec Development
 * %%
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * #L%
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.head.CssUrlReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple Wicket component that renders a Vue app. It renders its model (typically a HashMap) as the data for
 * the Vue app. It automatically inserts a method named "cb" in the Vue app which invokes the
 * {@link #onVueEvent(AjaxRequestTarget, Map)} after updating the model with the current data,
 * then you can make changes to the model and/or refresh other Wicket ajax components. At the end
 * of the request, the Vue app will be updated with the model again.
 */
public class WicketVueApp extends Panel {
  private static final String PARAM_NAME_EVENT_DATA = "eventData";
  private final ScriptElement vueJs;
  private final ScriptElement quasarJs;
  private boolean renderJsScriptElems;
  private boolean renderCSSLinkElems;
  private final IModel<? extends Map<String, Object>> state;
  private final AbstractDefaultAjaxBehavior ajaxBe;
  private final String appDivId;
  private final String vueMethods;
  private final List<String> useLibs;
  private final List<String> cssUrls;

  /**
   * Renders a Vue app. The Vue template should be provided in the body of the component in the markup.
   * @param id  the Wicket ID
   * @param state a Map containing the data variables for the Vue app. Typically this is {@link HashMap}, which is Serializable as required by Wicket.
   */
  public WicketVueApp(String id, IModel<? extends Map<String, Object>> state) {
    this(id, state, "");
  }

  /**
   * Renders a Vue app. The Vue template should be provided in the body of the component in the markup.
   * @param id  the Wicket ID
   * @param state a Map containing the data variables for the Vue app
   * @param vueMethods the Vue app's methods in js code in a String
   */
  public WicketVueApp(String id, IModel<? extends Map<String, Object>> state, String vueMethods) {
    super(id, state);
    this.state = state;
    this.vueMethods = vueMethods;
    useLibs= new ArrayList<>();
    cssUrls= new ArrayList<>();
    renderJsScriptElems=true;
    renderCSSLinkElems=true;
    vueJs=makeScriptElemForVue("vueJs");
    add(vueJs);
    quasarJs=makeScriptElemForQuasar("quasarJs");
    add(quasarJs);
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
        refresh(target);
      }
    };
    add(ajaxBe);
    WebMarkupContainer appDiv = new WebMarkupContainer("appDiv");
    appDiv.setOutputMarkupId(true);
    appDivId = appDiv.getMarkupId();
    add(appDiv);
    Label jsLbl = new Label("jsCode", new PropertyModel<String>(this, "jsCode"));
    jsLbl.setEscapeModelStrings(false);
    add(jsLbl);
  }

  public String getTemplateFromBody() {
    IMarkupFragment markup=getMarkup();
    StringBuffer vueTmpl=new StringBuffer();
    for (int i=1; i<markup.size()-1; i++) {
      vueTmpl.append(markup.get(i).toString());
    }
    return vueTmpl.toString();
  }

  /**
   * Set this to false if you don't want it to output the &lt;script&gt; elements
   * for Vue js and Quasar js (if Quasar support is enabled). By default, this is true.
   *
   * @param render
   */
  public void setRenderJsScriptElems(boolean render) {
    this.renderJsScriptElems = renderJsScriptElems;
    this.vueJs.setVisible(render);
    this.quasarJs.setVisible(render);
  }

  /**
   * Set this to false if you don't want it to output the &lt;link&gt; elements
   * for Quasar's CSS file (if Quasar support is enabled). By default, this is true.
   * @param renderCSSLinkElems
   */
  public void setRenderCSSLinkElems(boolean renderCSSLinkElems) {
    this.renderCSSLinkElems = renderCSSLinkElems;
  }

  public ScriptElement makeScriptElemForVue(String wicketId) {
    return new ScriptElement(wicketId, getVueURL());
  }
  public ScriptElement makeScriptElemForQuasar(String wicketId) {
    return new ScriptElement(wicketId, getQuasarURL());
  }

  /**
   * Call this to enable the use of Quasar. By default, you enable Quasar support,
   * it will automatically include Quasar's js and CSS. If you want, you can
   * prevent this using {@link #renderJsScriptElems} and {@link #renderCSSLinkElems}.
   */
  public void useQuasar() {
    useLibs.add("Quasar");
    cssUrls.add("https://cdn.jsdelivr.net/npm/quasar@2.14.2/dist/quasar.css");
  }

  /**
   * This returns the URL to Vue js to be used by this component. By default, it
   * returns the URL to Vue js hosted on a CDN. If you want to use another version,
   * you can overwrite this method.
   *
   * @return the URL to Vue js to be used
   */
  public String getVueURL() {
    return "https://cdn.jsdelivr.net/npm/vue@3/dist/vue.global.prod.js";
  }
  /**
   * This returns the URL to Quasar js to be used by this component. By default, it
   * returns the URL to Quasar js hosted on a CDN. If you want to use another version,
   * you can overwrite this method.
   *
   * @return the URL to Quasar js to be used
   */
  public String getQuasarURL() {
    return "https://cdn.jsdelivr.net/npm/quasar@2.14.2/dist/quasar.umd.js";
  }
  @Override
  public void renderHead(IHeaderResponse response) {
    if (renderCSSLinkElems) {
      for (String url : cssUrls) {
        response.render(CssUrlReferenceHeaderItem.forUrl(url));
      }
    }
  }

  /**
   * If your Vue app uses other 3rd party component libraries, for this component can generate
   * the <code>app.use(xxx)</code> line correctly, you need to add its name (<code>xxx</code> in this
   * example) to the list.
   * @return
   */
  public List<String> getUseLibs() {
    return useLibs;
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

  /**
   * Generates the js code for the Vue app. Usually you shouldn't call it or override it,
   * but if you want to do some special customizations, you can override it and twist
   * the returned code.
   * @return the complete js code for the Vue app
   */
  public String getJsCode() {
    StringBuffer useLibsCode=new StringBuffer();
    for (String u: useLibs) {
      useLibsCode.append(String.format("app.use(%s);", u));
    }
    TextTemplate tt = new PackageTextTemplate(this.getClass(), "WicketVueApp.js");
    Map<String, String> bindings = new HashMap<>();
    bindings.put("initData", getStateAsJsObj());
    bindings.put("ajaxCall", ajaxBe.getCallbackFunctionBody().toString());
    bindings.put("appDivId", appDivId);
    bindings.put("vueTemplate", getTemplateFromBody());
    bindings.put("vueMethods", vueMethods);
    bindings.put("useLibs", useLibsCode.toString());
    String jsCode = tt.asString(bindings);
    return jsCode;
  }

  /**
   * If your Wicket page is handling an ordinary ajax request and you want to
   * refresh this WicketVueApp component, call this method and pass the
   * {@link AjaxRequestTarget} to it.
   *
   * @param target
   */
  public void refresh(AjaxRequestTarget target) {
    target.appendJavaScript(String.format("Object.assign(document.getElementById('%s').myVueInst.$data, %s)", appDivId, getStateAsJsObj()));
  }

  /**
   * If your Vue app calls the "cb" method, it sends an ajax request back to
   * this component, which updates the model with the current data and calls
   * this method for you to do your own work. Here, you can make changes to
   * the model and/or refresh other Wicket ajax components. At the end
   * of the request, the Vue app will be updated with the model again automatically.
   * @param target the ajax target
   * @param data if your js code pass custom data to the "cb" method, it will be available here.
   */
  public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {

  }
}
