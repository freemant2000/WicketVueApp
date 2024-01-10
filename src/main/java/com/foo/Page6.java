package com.foo;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DoubleConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Page6 extends WebPage {
  private Double n = 8412.46;

  public Page6() {
    FeedbackPanel fb = new FeedbackPanel("fb");
    fb.setOutputMarkupId(true);
    add(fb);
    Form f = new Form("f");
    add(f);
    IConverter<Double> c=new DoubleConverter() {
      @Override
      public NumberFormat getNumberFormat(Locale locale) {
        return new DecimalFormat("#,##0.00");
      }
    };
    TextField<Double> tf = new TextField<Double>("tf", new PropertyModel<>(this, "n")) {
      @Override
      protected IConverter<Double> createConverter(Class<?> type) {
        return c;
      }
    };
    tf.setRequired(true);
    f.add(tf);
    AjaxSubmitLink submit = new AjaxSubmitLink("submit", f) {

      @Override
      protected void onSubmit(AjaxRequestTarget target) {
        target.add(fb);
        target.add(f);
      }
      @Override
      protected void onError(AjaxRequestTarget target) {
        target.add(fb);
        target.add(f);
      }
    };
    f.add(submit);
  }
}
