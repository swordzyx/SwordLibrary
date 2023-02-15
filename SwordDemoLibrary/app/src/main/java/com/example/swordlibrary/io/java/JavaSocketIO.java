package com.example.swordlibrary.io.java;

import android.os.Build;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class JavaSocketIO {
  public static void main(String[] args) {
    
  }

  /**
   * 在本机的 80 端口启动一个监听，监听连接过来的客户端
   */
  public void startSocketServerIO() {
    try(ServerSocket server = new ServerSocket(80)) {
      Socket client = server.accept();
      //读取客户端发过来的消息
      BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
      //向客户端发送消息
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 在本机 [port] 端口开启监听
   * @param port 端口
   */
  public void startSocketServerNIO(int port) {
    try {
      ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        serverSocketChannel.bind(new InetSocketAddress(80));
        //非阻塞
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        
        while (true) {
          selector.select();
          for (SelectionKey key : selector.keys()) {
            if (key.isAcceptable()) {
              SocketChannel socketChannel = serverSocketChannel.accept();
              ByteBuffer buffer = ByteBuffer.allocate(1024);
              while(socketChannel.read(buffer) != -1) {
                buffer.flip();
                System.out.println("收到客户端 " + socketChannel.getRemoteAddress().toString() + " 的消息：" + Charset.defaultCharset().decode(buffer));
                buffer.clear();
              }
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
