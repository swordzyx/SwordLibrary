package sword.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 通过传统 IO 实现文件操作
 */
public class JavaFileIO {
    private static final String tag = "JavaIO";

    public static void main(String[] args) {
        //System.out.println(readFile("./gradle.properties"));
        int copyBytes = copyFile(new File("./gradle.properties"), new File("./gradle_copy.properties"));
        System.out.println("拷贝 " + copyBytes + " 字节");
    }

    public static String readFile(String filePath) {
        StringBuilder fileInfo = new StringBuilder();

        readFile(filePath, fileInfo::append);

        return fileInfo.toString();
    }


    public static void readFile(String filePath, Callback callback) {
        if (callback == null) {
            return;
        }
        
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                callback.onReadLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int copyFile(File sourceFile, File targetFile) {
        if (sourceFile == null || targetFile == null) {
            return -1;
        }
        if (!sourceFile.exists()) {
            System.out.println("源文件不存在");
            return -1;
        }

        File parentFile = targetFile.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }

        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            if (targetFile.exists()) {
                targetFile.delete();
            }
            targetFile.createNewFile();

            inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));
            return copyFile(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    /**
     * 文件拷贝。从 [input] 读取字节，写入 [output]
     *
     * @param input  源文件输入流
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
        System.out.println("拷贝 " + byteCopied + " 字节总共耗时: " + (System.currentTimeMillis() - startTime) + "ms");
        return byteCopied;
    }
    
    public interface Callback {
        void onReadLine(String line);
    }
}
