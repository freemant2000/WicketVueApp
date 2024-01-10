package com.foo;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;

public abstract class MySubmitLink extends AjaxSubmitLink {

  private final Form<?> form;

  public MySubmitLink(String id, final Form<?> form)
  {
    super(id, form);
    this.form=form;
  }
  protected AjaxFormSubmitBehavior newAjaxFormSubmitBehavior(String event) {
    return new AjaxFormSubmitBehavior(form, event) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void onError(AjaxRequestTarget target) {
        MySubmitLink.this.onError(target);
      }

      @Override
      protected Form<?> findForm() {
        return MySubmitLink.this.getForm();
      }

      @Override
      public boolean getDefaultProcessing() {
        return MySubmitLink.this.getDefaultFormProcessing();
      }

      @Override
      protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
        super.updateAjaxAttributes(attributes);
        MySubmitLink.this.updateAjaxAttributes(attributes);
      }

      @Override
      protected void onEvent(AjaxRequestTarget target) {
        onBeforeProcessEvent(target);
        super.onEvent(target);
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target) {
        MySubmitLink.this.onSubmit(target);
      }

      @Override
      protected void onAfterSubmit(AjaxRequestTarget target) {
        MySubmitLink.this.onAfterSubmit(target);
      }

      @Override
      public boolean getStatelessHint(Component component) {
        return MySubmitLink.this.getStatelessHint();
      }
    };
  }

  abstract protected void onBeforeProcessEvent(AjaxRequestTarget target);
}
