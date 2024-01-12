package com.ttdev;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;

public class ScriptElement extends Label {
  public ScriptElement(String id, String src) {
    super(id, src);
    add(new AttributeModifier("src", src));
  }

  @Override
  protected void onComponentTag(ComponentTag tag) {
    super.onComponentTag(tag);
    if (!tag.getName().equalsIgnoreCase("script")) {
      throw new WicketRuntimeException(String.format("<script> expected but got <%s>", tag.getName()));
    }
  }
}
