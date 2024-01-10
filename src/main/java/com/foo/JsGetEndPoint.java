package com.foo;

import org.apache.wicket.Component;
import org.apache.wicket.IRequestListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.resource.CoreLibrariesContributor;

public abstract class JsGetEndPoint extends Behavior implements IRequestListener {
  private final String jsFuncName;
  private Component component;

  public JsGetEndPoint(String jsFuncName) {
    this.jsFuncName = jsFuncName;
  }

  @Override
  public void bind(Component component) {
    this.component = component;
  }

  @Override
  public void unbind(Component component) {  this.component = null;  }

  @Override
  public boolean rendersPage() {
    return false;
  }

  public CharSequence getUrl() {
    return component.urlForListener(this, new PageParameters());
  }

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    CoreLibrariesContributor.contributeAjax(component.getApplication(), response);
    CharSequence url = getUrl();
    String script = String.format("function %s(data) { return new Promise((resolve, reject)=>$.get('%s', data).done(resolve).fail(reject)); }",
        jsFuncName, url);
    response.render(new JavaScriptContentHeaderItem(script, jsFuncName, ""));
  }

  @Override
  public void onRequest() {
    String jsonData = onDataRequest();
    TextRequestHandler th = new TextRequestHandler("application/json", "utf-8", jsonData);
    RequestCycle.get().scheduleRequestHandlerAfterCurrent(th);
  }

  abstract protected String onDataRequest();
}