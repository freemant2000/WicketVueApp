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
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import java.util.HashMap;
import java.util.Map;

public class RouteDemo extends WebPage {

  private final HashMap<String, Object> state;
  private Label msg;

  public RouteDemo() {
    state = new HashMap<>();
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state),
        "<button @click='cb(\"onB1Click\")'>OK</button><button @click='cb(\"onB2Click\")'>Cancel</button>") {
      public void onB1Click(AjaxRequestTarget target, Map<String, Object> data) {
        msg.setDefaultModelObject("B1 clicked");
        target.add(msg);
      }
      public void onB2Click(AjaxRequestTarget target, Map<String, Object> data) {
        msg.setDefaultModelObject("B2 clicked");
        target.add(msg);
      }
    };
    add(vwa);
    msg=new Label("msg", new Model<String>());
    msg.setOutputMarkupId(true);
    add(msg);
  }
}
