package com.example.io;

import android.annotation.SuppressLint;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.logging.SocketHandler;

public class Main {
    public static void main(String[] args) {
        //io1();
        //io2();
        //io3();
        //io4();
        //io5();
        //io6();
        //io7();
        //io8();
        //io9();
        io10();
    }

    @SuppressLint("NewApi")
    private static void io10() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8099));
            //设置为非阻塞式
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            //将 ServerSocketChannel 注册到 Selector 中，当有终端连接到 ServerSocketChannel 时，会自动通知到 selector，通过 selector.select() 方法接收通知，SelectionKey 表示接收所有的 ACCEPT 事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while(true) {
                //select() 是一个阻塞方法，这是没办法的，做这种网络编程，肯定有一个地方是要进行等待的，不过使用 selector 可以实现多个 ServerSocketChannel 注册到同一个 Selector，这样可以同时监听多个 ServerSocketChannel 的连接。所有的 Channel 都可以往同一个 Selector 中注册，这样不同的代码就不用写到多个不同的平行线程中，使用一个线程可以处理多个 Channel 的连接
                selector.select();
                //遍历所有发生的事件
                for (SelectionKey key : selector.selectedKeys()) {
                    //表示发生了 ACCEPT 事件，且有终端连接到了 ServerSocketChannel
                    if (key.isAcceptable()) {
                        //ServerSocketChannel 为非阻塞式，但 SocketChannel 依然为阻塞式
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        while (socketChannel.read(byteBuffer) != -1) {
                            //每次对 ByteBuffer 进行操作之后都要进行 flip()（翻页） 操作
                            if (byteBuffer.get(byteBuffer.position() - 1) == 10) {
                                byteBuffer.flip();
                                System.out.println(Charset.defaultCharset().decode(byteBuffer));
                                byteBuffer.flip();
                                socketChannel.write(byteBuffer);
                                byteBuffer.clear();
                            }
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private static void io9() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //绑定服务器 ip 及端口，此处使用本机作为测试，填入一个端口即可，默认 IP 为 127.0.0.1
            //InetSocketAddress 另外一个可以指定 IP 地址的构造方法：public InetSocketAddress(InetAddress addr, int port)
            serverSocketChannel.bind(new InetSocketAddress(8099));
            SocketChannel socketChannel = serverSocketChannel.accept();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while (socketChannel.read(byteBuffer) != -1) {
                if (byteBuffer.get(byteBuffer.position() - 1) == 10) {
                    byteBuffer.flip();
                    System.out.println(Charset.defaultCharset().decode(byteBuffer));
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);
                    byteBuffer.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        io8();
    }

    private static void io8() {
        try {
            RandomAccessFile file = new RandomAccessFile("./io/test.txt", "r");
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);
            //读完之后，position 指向的是 buffer 中有效内容的最后一位，将 position 赋值给 limit，用于仅读取有效内容的长度。然后将 position 置为 0 ，即从第 0 位开始读取 Buffer 中的内容。
            //buffer.flip() 等价于 buffer.limit(buffer.position()); buffer.position(0)
            buffer.flip();
            System.out.println(Charset.defaultCharset().decode(buffer));
            //buffer.clear() 等价于 buffer.limit(buffer.capacity()); buffer.position(0);
            buffer.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void io2() {
        try (InputStream inputStream = new FileInputStream("./io/test.txt")) {
            //从 ./io/test.txt 中读一个字节，并打印到控制台
            System.out.println((char) inputStream.read());
            System.out.println((char) inputStream.read());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }/* finally {
            //执行 close 时还要进行 try...catch，这是标准写法
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public static void io1() {
        try (OutputStream output = new FileOutputStream("./io/test.txt")) {
            //往 ./io/test.txt 中写一个字节
            output.write('a');
            output.write('b');
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void io3() {
        try (InputStream inputStream = new FileInputStream("./io/test.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            System.out.println(reader.readLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void io4() {
        try (OutputStream outputStream = new FileOutputStream("./io/test.txt");
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            byte[] content = "abcdefg, 入海，庄周晓梦迷蝴蝶".getBytes();
            bufferedOutputStream.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void io5() {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("./io/test.txt"));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("./io/new_test.txt"))) {
            byte[] content = new byte[1024];
            int count;
            //当 inputStream.read() 返回 -1 ，表示未读到任何内容
            while ((count = inputStream.read(content)) != -1) {
                //从 test.txt 中读到多少内容，就往 new_test.txt 中写多少内容，直到从 test.txt 中读不到内容了为止。
                outputStream.write(content, 0, count);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端，发送请求，获取响应
     */
    private static void io6() {
        //建 TCP 链接
        try (Socket socket = new Socket("hencoder.com", 80);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            //定位到目标主机以及端口，这里的目标主机可以是域名，也可以是 IP 地址
            //Socket socket = new Socket("hencoder.com", 80);
            //Http 是纯文本，因此针对字符而不必针对字节
            //BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //对于网络请求，目标主机只有在看到 2 个换行之后才认为请求结束。

            //2. 写请求报文。两个换行（\n）表示请求结束
            writer.write("GET / HTTP/1.1 \n" +
                    "Host: www.example.com\n\n");
            writer.flush();

            //3. 读取响应
            String content;
            while ((content = reader.readLine()) != null) {
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //在浏览器输入 127.0.0.1:9999 一直显示 “127.0.0.1 拒绝了我们的连接请求”
    private static void io7() {
        try (ServerSocket server = new ServerSocket(9999);
             Socket socket = server.accept();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            writer.write("HTTP/1.1 200 OK\n" +
                    "Date: Mon, 23 May 2005 22:38:34 GMT\n" +
                    "Content-Type: text/html; charset=UTF-8\n" +
                    "Content-Length: 138\n" +
                    "Last-Modified: Wed, 08 Jan 2003 23:11:55 GMT\n" +
                    "Server: Apache/1.3.3.7 (Unix) (Red-Hat/Linux)\n" +
                    "ETag: \"3f80f-1b6-3e1cb03b\"\n" +
                    "Accept-Ranges: bytes\n" +
                    "Connection: close\n" +
                    "\n" +
                    "<html>\n" +
                    "  <head>\n" +
                    "    <title>An Example Page</title>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <p>Hello World, this is a very simple HTML document.</p>\n" +
                    "  </body>\n" +
                    "</html>\n\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
