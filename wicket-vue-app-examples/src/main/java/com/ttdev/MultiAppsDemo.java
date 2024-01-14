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
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.INamedParameters;

import java.util.HashMap;
import java.util.Map;

public class MultiAppsDemo extends WebPage {
  public MultiAppsDemo() {
    HashMap<String, Object> state = new HashMap<>();
    state.put("product", "Pen");
    state.put("price", 4);
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state), "<span :style='sty'>{{product}} on sale for {{price}}<span>");
    add(vwa);
    HashMap<String, Object> state2 = new HashMap<>();
    WicketVueApp vwa2=new WicketVueApp("wva2", new Model(state2), "This is another Vue app: <button @click='cb();'>Click me</button>") {
      @Override
      public void onVueEvent(AjaxRequestTarget target, Map<String, Object> data) {
        state.put("price", (Integer)state.get("price")+1);
        vwa.refresh(target);
      }
    };
    vwa2.setRenderJsScriptElems(false);
    add(vwa2);
  }
}
