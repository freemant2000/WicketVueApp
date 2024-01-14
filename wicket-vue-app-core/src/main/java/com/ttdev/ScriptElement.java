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
