package com.example.utilclass;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密，解密，提取数据摘要
 */
public class Encryption {
    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA1";
    
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 将字节转成 16 进制内容，以字符串的形式输出
     * 
     * >>> 无符号右移，空位以 0 补齐
     */
    private static String toHexString(byte[] input) {
        if (input == null || input.length <= 0) {
            return null;
        }
        int len = input.length;
        StringBuilder sb = new StringBuilder(len*2);
        int temp = 0;
        for (byte b : input) {
            temp = b;
            sb.append(HEX_CHARS[(temp & 0xf0)>>>4]);
            sb.append(HEX_CHARS[(temp & 0x0f)]);
        }
        return sb.toString();
    }
    
    private static boolean checkInputs(byte[] input, int offset, int len) {
        if (input == null || offset < 0 || len < 0) {
            return false;
        }
        if (input.length <= 0) {
            return false;
        }
        
        //检查偏移是否超过了数组的长度
        if (offset > input.length - 1) {
            return false;
        }

        return offset + len <= input.length;
    }

    /**
     * 提取字节数组 content 的摘要
     */
    private static byte[] DIGEST(byte[] contents, int offset, int len, String hashType) {
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            md.update(contents, offset, len);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对 input 提取摘要之后，将摘要转成 16 进制的字符串
     */
    private static String DIGEST_HEX(byte[] input, int offset, int len, String hashType) {
        return toHexString(DIGEST(input, offset, len, hashType));
    }

    /**
     * 提取 input 摘要，然后将摘要转成 16 进制字符串。 
     */
    private static String DIGEST_HEX_String(String input, String hashType) {
        if (input == null || TextUtils.isEmpty(input)) {
            return null;
        }
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        return DIGEST_HEX(bytes, 0, bytes.length, hashType);
    }

    /**
     * 提取文件的摘要，然后将摘要转成十六进制字符串。
     */
    private static String DIGEST_HEX_FILE(File file, String hashType) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            FileInputStream input = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) != -1) {
                md.update(buf, 0, len);
            }
            input.close();
            return toHexString(md.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String md5(String content) {
        return DIGEST_HEX_String(content, MD5);
    }
    
    public static String sha1(String content) {
        return DIGEST_HEX_String(content,SHA1);
    }
}
