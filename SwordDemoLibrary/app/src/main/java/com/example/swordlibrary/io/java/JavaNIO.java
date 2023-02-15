package com.example.swordlibrary.io.java;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class JavaNIO {
  public void readPath(String filePath) {
    try {
      readRandomFile(new RandomAccessFile(filePath, "r"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
  
  public void readRandomFile(RandomAccessFile file) {
    readFileChanel(file.getChannel());
  }
  
  public void readFileChanel(FileChannel fileChannel) {
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    try {
      //从 FileChannel 读取字节到 buffer 中，感觉 fileChannel.readTo(buffer) 会不会更直观一些
      while (fileChannel.read(buffer) > 0) {
        //将 limit 置为 position，将 position 置为 0
        buffer.flip(); //这一行等价于上面两行
        //使用 UTF-8 对 buffer 中的字节进行编码，也就是转成一个字符串
        System.out.println(Charset.defaultCharset().decode(buffer));
        //重置 ByteBuffer，将 position 置为 0，limit 置为 capacity
        buffer.clear();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  
  
}
