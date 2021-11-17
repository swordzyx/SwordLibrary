package com.example.utilclass;

/**
 * 加密，解密，提取数据摘要
 */
public class Encryption {
    
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
    
    private static boolean checkInputs()
    
    public static String md5(String content) {
        
    }
}
