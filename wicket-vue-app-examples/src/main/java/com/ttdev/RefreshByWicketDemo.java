package com.ttdev;

/*-
 * #%L
 * wicket-vue-app-examples
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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import java.util.HashMap;
import java.util.Map;

public class RefreshByWicketDemo extends WebPage {
  private final HashMap<String, Object> state;

  public RefreshByWicketDemo() {
    state = new HashMap<>();
    state.put("n", 0);
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state),"<span>{{n}}</span>");
    add(vwa);
    AjaxLink ok=new AjaxLink<Void>("ok") {

      @Override
      public void onClick(AjaxRequestTarget target) {
        state.put("n", (Integer)state.get("n")+1);
        vwa.refresh(target);
      }
    };
    add(ok);
  }
}
