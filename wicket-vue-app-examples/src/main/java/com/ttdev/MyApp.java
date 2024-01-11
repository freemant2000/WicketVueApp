package com.ttdev;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class MyApp extends WebApplication {
  @Override
  public Class<? extends Page> getHomePage() {
    return P3.class;
  }
}
