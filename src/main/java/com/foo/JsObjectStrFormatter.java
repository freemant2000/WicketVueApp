package com.foo;

import java.util.List;
import java.util.Map;

public class JsObjectStrFormatter {
  public String formatAsJSObject(Map<String, Object> map) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");

    for (Map.Entry<String, Object> entry : map.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      if (sb.length()>1) {
        sb.append(", ");
      }
      sb.append("\"").append(key).append("\": ");
      sb.append(formatAsJSValue(value));
    }
    sb.append("}");
    return sb.toString();
  }
  public String formatAsJSValue(Object value) {
    StringBuilder sb = new StringBuilder();
    if (value instanceof String) {
      sb.append("\"").append(value).append("\"");
    } else if (value instanceof Map) {
      sb.append(formatAsJSObject((Map<String, Object>) value));
    } else if (value instanceof List) {
      sb.append(formatAsJSArray((List<Object>) value));
    } else {
      sb.append(value);
    }
    return sb.toString();
  }
  public String formatAsJSArray(List<Object> list) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (Object element : list) {
      if (sb.length()>1) {
        sb.append(", ");
      }
      sb.append(formatAsJSValue(element));
    }
    sb.append("]");
    return sb.toString();
  }
}
