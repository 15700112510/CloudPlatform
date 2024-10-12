package com.cloud.common.core.utils;

import com.cloud.common.core.constant.StrTypeConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 中文字符及特殊字符判断工具类
 */
public class StrTypeUtil {

    // 判断某个字符是否为中文字符
    public static boolean isChinese(char ch) {
        return String.valueOf(ch).matches(StrTypeConstant.CHINESE_CHARACTER);
    }

    // 判断某个字符串中是否包含中文字符
    public static boolean containChinese(String str) {
        // 字符串为空
        if (str == null || str.length() == 0) {
            return false;
        }
        // 字符串转字符数组
        char[] chars = str.toCharArray();
        // 遍历数组，若有汉字则返回true
        for (char ch : chars) {
            if (isChinese(ch)) {
                return true;
            }
        }
        // 若无汉字返回false
        return false;
    }

    // 判断字符串是否包含特殊字符，也可直接判断单个字符
    public static boolean containSpecialChar(String str) {
        Pattern p = Pattern.compile(StrTypeConstant.SPECIAL_CHARACTER);
        Matcher m = p.matcher(str);
        return m.find();
    }
}
