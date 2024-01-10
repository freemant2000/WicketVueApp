package com.foo;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;

public abstract class JsPostEndPoint extends AbstractDefaultAjaxBehavior  {
  private final String jsFuncName;
    public JsPostEndPoint(String jsFuncName) {
    this.jsFuncName = jsFuncName;
  }

  @Override
  protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
    AjaxCallListener listener = new AjaxCallListener();
    listener.onSuccess("resolve(data);");
    listener.onFailure("reject(jqXHR);");
    attributes.getAjaxCallListeners().add(listener);
    attributes.getDynamicExtraParameters().add("return dataParams;");
  }

  @Override
  public void renderHead(Component component, IHeaderResponse response) {
    super.renderHead(component, response);
    CharSequence fb = getCallbackFunctionBody();
    String script = String.format("function %s(dataParams) { return new Promise((resolve, reject) => {%s;}); }",
        jsFuncName, fb);
    response.render(new JavaScriptContentHeaderItem(script, jsFuncName, ""));
  }

}
