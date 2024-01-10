package com.foo;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

public class Page5 extends WebPage {
  private Integer n = 0;

  public Page5() {
    FeedbackPanel fb = new FeedbackPanel("fb");
    fb.setOutputMarkupId(true);
    add(fb);
    Form f = new Form("f");
    add(f);
    TextField<Integer> tf = new TextField<Integer>("tf", new PropertyModel<>(this, "n"));
    f.add(tf);
    MySubmitLink submit = new MySubmitLink("submit", f) {

      @Override
      protected void onBeforeProcessEvent(AjaxRequestTarget target) {
        tf.setRequired(true);
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target) {
        tf.setRequired(false);
        target.add(fb);
      }
      @Override
      protected void onError(AjaxRequestTarget target) {
        tf.setRequired(false);
        target.add(fb);
      }
    };
    f.add(submit);
  }
}
