package com.foo;

public class Book {
  private String title;
  private int price;

  public Book(String title, int price) {
    this.title = title;
    this.price = price;
  }

  public String getTitle() {
    return title;
  }

  public int getPrice() {
    return price;
  }
}
