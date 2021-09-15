package com.example.utilclass;

public class TextUtil {
    /**
     * 判断字符串是否包含数字，不使用正则表达式 
     */
    public static boolean hasDigit(String text){
        char[] textChars = text.toCharArray();
        for (char c:textChars) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 使用正则表达式判断字符串是否包含数字
     */
    private static final String CONTAIN_NUMBER_REGEX = ".*[0-9].*";
    public static boolean hasDigitByRegex(String text) {
        return text.matches(CONTAIN_NUMBER_REGEX);
    }

    /**
     * 使用正则表达式判断字符串是否仅包含中文 
     */
    private static final String ONLY_CHINESE_REGEX = "[\u4e00-\u9fa5]";
    private static boolean isChinese(String text) {
        return text.matches(ONLY_CHINESE_REGEX);
    }

    /**
     * 身份证号校验
     */
    /*private static final String ID_NUMBER_REGEX = ""
    private static boolean isIdNumberValid(String idNumber) {
        
    }*/

}
