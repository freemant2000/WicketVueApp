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
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServerSideEventHandlingDemo extends WebPage {

  private final HashMap<String, Object> state;
  private Label lastUpd;
  public ServerSideEventHandlingDemo() {
    state = new HashMap<>();
    state.put("n", 0);
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state),
        "<span @click='cb()'>Click me: {{n}}<span>") {
      @Override
      public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {
        state.put("n", (Integer)state.get("n")+1);
        target.add(lastUpd);
      }
    };
    add(vwa);
    lastUpd=new Label("lastUpd", LambdaModel.of(()->new Date().toString()));
    lastUpd.setOutputMarkupId(true);
    add(lastUpd);
  }
}
