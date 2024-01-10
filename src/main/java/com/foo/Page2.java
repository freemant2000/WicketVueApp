package com.foo;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.IRequestParameters;

import java.util.HashMap;
import java.util.Map;

public class Page2 extends WebPage {
  private Map<String, Integer> prices;
  public Page2() {
    prices=new HashMap<String, Integer>();
    prices.put("pen", 3);
    prices.put("pencil", 5);
    prices.put("ruler", 6);
    add(new JsGetEndPoint("ajaxGetPrice") {
      @Override
      protected String onDataRequest() {
        IRequestParameters params = getRequest().getRequestParameters();
        String n=params.getParameterValue("n").toString();
        int price=prices.getOrDefault(n, -1);
       return Integer.toString(price);
      }
    });
  }
}
