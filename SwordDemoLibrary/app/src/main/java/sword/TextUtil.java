package sword;

import java.util.regex.Pattern;

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
     * 判断输入字符串是否为纯数字
     */
    private static final String DIGIT_REGEX = "[0-9]+";
    public static boolean isDigit(String input) {
        return input.matches(DIGIT_REGEX);
    }

    /**
     * 判断输入字符串是否为纯字母
     */
    public static final String LETTER_REGEX = "[a-zA-Z]+";
    public static boolean onlyLetter(String input) {
        return input.matches(LETTER_REGEX);
    }
    
    private static final String CONTAIN_LETTER_REGEX = ".*[a-zA-z].*";
    public static boolean hasLetter(String input) {
        return input.matches(CONTAIN_LETTER_REGEX);
    }
    

    /**
     * 使用正则表达式判断字符串是否仅包含中文 
     */
    private static final String ONLY_CHINESE_REGEX = "[\u4e00-\u9fa5]";
    public static boolean onlyChinese(String text) {
        return text.matches(ONLY_CHINESE_REGEX);
    }

    /**
     * 这个方法是冗余的，isChinese(String text) 可以达到同样的效果，但是只需匹配一次
     * @param text
     * @return
     */
    public static boolean hasChinese(String text) {
        Pattern pattern = Pattern.compile(ONLY_CHINESE_REGEX);
        for(int i=0; i<text.length(); i++) {
            //创建模式匹配器，将指定的输入字符串与该匹配器进行匹配
            if (!pattern.matcher(String.valueOf(text.charAt(i))).find()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断输入字符串是否包含特殊字符
     */
    private static final String SPECIAL_REGEX = "[ _`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\n|\\r|\\t";
    public static boolean hasSpecial(String str) {
        return str.matches(SPECIAL_REGEX);
    }


    /**
     * 是否以特殊字符开头
     */
    public static final String DEFAULT_QUERY_REGEX = "[!$^&*+=|{}';'\",<>/?~！#￥%……&*——|{}【】‘；：”“'。，、？]";
    public static boolean startWithSpecial(String input) {
        if(!input.matches(DEFAULT_QUERY_REGEX)) {
            return false;
        }
        for (char ch : DEFAULT_QUERY_REGEX.toCharArray()) {
            if (ch == input.charAt(0)) {
                return true;
            }
        }
        return false;
    }



    /**
     * 判断输入字符串是否仅包含英文字母，数字和汉字
     */
    private static final String CHINESE_LETTER_DIGIT_REGEX = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
    public static boolean hasChineseAndDigitAndLetter(String text) {
        return text.matches(CHINESE_LETTER_DIGIT_REGEX);
    }

    /**
     * 判断字符串是否仅包含数字和字母
     * ^: 匹配输入字符串的开始位置
     * $: 匹配输入字符串的结尾位置
     */
    private static final String LETTER_AND_DIGIT_REGEX = "^[a-z0-9A-Z]+$";
    public static boolean hasLetterAndDigit(String str) {
        return str.matches(LETTER_AND_DIGIT_REGEX);
    }


    /**
     * 判断输入字符串是否仅包含中文字符和字母
     */
    private static final String CHINESE_LETTER_REGEX = "([\u4e00-\u9fa5]+|[a-zA-Z]+)";
    public static boolean hasLetterAndChinese(String input) {
        return input.matches(CHINESE_LETTER_REGEX);
    }

    /**
     * 判断输入字符串是否包含标点符号
     */
    private static final String PUNCTUATION_REGEX = ".*\\p{P}.*";
    private static boolean hasPunctuation(String input) {
        return input.matches(PUNCTUATION_REGEX);
    }
    
    

}
