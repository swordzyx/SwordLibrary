package sword;

import java.util.ArrayList;

public class Hello {
  public static void main(String[] args) {
    System.out.println("Hello JVM");
  }
  
  void foo(ArrayList<String> list) {
    new Inner<String>();
  }
  
  class Inner<T> {
    
  }
}
