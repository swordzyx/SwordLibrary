package com.sword.mylibrary.sort;

public class Student implements Comparable<Student> {
  final int age;
  final int score;
  
  Student(int age, int score) {
    this.age = age;
    this.score = score;
  }

  @Override
  public int compareTo(Student o) {
    return this.age - o.age;
  }
}
