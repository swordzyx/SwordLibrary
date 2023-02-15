package com.example.swordlibrary.io.java;

import com.sword.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JavaFileIO {
  private static final String tag = "JavaIO";
  public static String readFile(String filePath) {
    StringBuilder fileInfo = new StringBuilder();

    File file = new File(filePath);
    if (!file.exists()) {
      return null;
    }

    try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
      String message;
      while ((message = fileReader.readLine()) != null) {
        fileInfo.append(message).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return fileInfo.toString();
  }

  /**
   * 文件拷贝。从 [input] 读取字节，写入 [output]
   * @param input 源文件输入流
   * @param output 目标文件输出流
   * @return 拷贝的字节数
   */
  public static int copyFile(BufferedInputStream input, BufferedOutputStream output) throws IOException {
    long startTime = System.currentTimeMillis();
    byte[] buffer = new byte[1024];
    int len;
    int byteCopied = 0;
    while ((len = input.read(buffer)) > 0) {
      output.write(buffer, 0, len);
      byteCopied += len;
    }
    LogUtil.debug(tag, "拷贝 " + byteCopied + " 字节总共耗时: " + (System.currentTimeMillis() - startTime) + "ms");
    return byteCopied;
  }
}
