package com.foo;

 interface Foo {
  int getX();
}

 class Bar {
  private int a=2;
  public void m() {
    a=4;
    Foo f=new Foo() {
      public int getX() { return a; }
    };
    a=5;
    System.out.println(f.getX());  }
}


public class Main {
  public static void main(String[] args) {
    new Bar().m();

  }
}
