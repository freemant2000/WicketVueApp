package com.foo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.wicket.markup.html.WebPage;
public class P1 extends WebPage {
  public P1() {
    Book b=new Book("python", 24);
    ObjectMapper om=new ObjectMapper();
    add(new JsGetEndPoint("getBook") {
      @Override
      protected String onDataRequest() {
        try {
          String s=om.writeValueAsString(b);
          return s;
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }
}
