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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeDemo extends WebPage {

  public static class Product {
    private int id;
    private String name;
    private int price;
    private List<Product> parts;

    public Product(int id, String name, int price) {
      this.id = id;
      this.name = name;
      this.price=price;
      this.parts=new ArrayList<>();
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getPrice() {
      return price;
    }

    public void setPrice(int price) {
      this.price = price;
    }

    public List<Product> getParts() {
      return parts;
    }

    public void setParts(List<Product> parts) {
      this.parts = parts;
    }
  }

  public TreeDemo() {
    List<Product> ps=new ArrayList<>();
    Product p=new Product(0, "writing tools", 9);
    p.getParts().add(new Product(1, "pen", 4));
    p.getParts().add(new Product(2, "pencil", 5));
    ps.add(p);
    p=new Product(3, "accessories", 12);
    p.getParts().add(new Product(4, "eraser", 3));
    p.getParts().add(new Product(5, "ruler", 9));
    ps.add(p);
    HashMap<Object, Object> state = new HashMap<>();
    state.put("ns", ps);
    WicketVueApp vwa=new WicketVueApp("wva", new Model(state)) ;
    vwa.useQuasar();
    add(vwa);
  }
}
