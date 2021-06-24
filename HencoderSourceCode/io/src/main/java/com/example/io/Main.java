package com.example.io;

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
import java.net.Socket;
import java.util.logging.SocketHandler;

public class Main {
    public static void main(String[] args) {
        //io1();
        //io2();
        //io3();
        //io4();
        //io5();
        io6();
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

    private static void io6() {
        try (Socket socket = new Socket("hencoder.com", 80);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            //定位到目标主机以及端口，这里的目标主机可以是域名，也可以是 IP 地址
            //Socket socket = new Socket("hencoder.com", 80);
            //Http 是纯文本，因此针对字符而不必针对字节
            //BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //对于网络请求，目标主机只有在看到 2 个换行之后才认为请求结束。

            writer.write("GET / HTTP/1.1 \n" +
                    "Host: www.example.com\n\n");
            writer.flush();

            String content;
            while ((content = reader.readLine()) != null) {
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
