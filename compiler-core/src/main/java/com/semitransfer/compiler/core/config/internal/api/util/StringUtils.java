package com.semitransfer.compiler.core.config.internal.api.util;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.semitransfer.compiler.core.config.internal.api.Constants.NUM_ONE;


/**
 * <p>
 * 字符串相关 判空\编码等
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class StringUtils {

    private StringUtils() {
    }


    /**
     * 判断字符串是否为空(增强版 空字符、null、undefined)
     *
     * @param string 指定字符串
     * @return null、undefined返回 true, 否则返回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean isEmptyEnhance(String string) {
        return (null == string || string.trim().length() == 0
                || "undefined".equals(string) || "null".equals(string));
    }

    /**
     * 判断字符串是否为空
     *
     * @param string 指定字符串
     * @return null 或 空字符串返回 true, 否则返回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean isEmpty(String string) {
        return (null == string || string.length() == 0);
    }


    /**
     * 判断字符串是否不为空(增强版 空字符、null、undefined)
     *
     * @param object object类型
     * @return 不为 空字符、null、undefined 且 长度大于 0 返回 true, 否则返回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean notEmptyEnhance(Object object) {
        return notEmptyEnhance(String.valueOf(object));
    }

    /**
     * 判断字符串是否不为空(增强版 空字符、null、undefined)
     *
     * @param string 指定字符串
     * @return 不为 空字符、null、undefined 且 长度大于 0 返回 true, 否则返回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean notEmptyEnhance(String string) {
        return string != null && string.trim().length() > 0
                && !"undefined".equals(string) && !"null".equals(string);
    }

    /**
     * 判断字符串是否不为空
     *
     * @param string 指定字符串
     * @return 不为 null 且 长度大于 0 返回 true, 否则返回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean notEmpty(String string) {
        return string != null && string.length() > 0;
    }


    /**
     * 判断所有的字符串是否都为空（增强版）
     *
     * @param strings 指定字符串数组获或可变参数
     * @return 所有字符串都为空返回 true, 否则放回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean isAllEmptyEnhance(String... strings) {
        if (null == strings) {
            return true;
        }
        for (String str : strings) {
            if (!isEmptyEnhance(str)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断所有的字符串是否都为空
     *
     * @param strings 指定字符串数组获或可变参数
     * @return 所有字符串都为空返回 true, 否则放回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean isAllEmpty(String... strings) {
        if (null == strings) {
            return true;
        }
        for (String str : strings) {
            if (!isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断所有的字符串是否都不为空(增强版)
     *
     * @param strings 指定字符串数组获或可变参数
     * @return 所有字符串都不为空返回 true, 否则放回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean isAllNotEmptyEnhance(String... strings) {
        if (null == strings) {
            return false;
        }
        for (String str : strings) {
            if (isEmptyEnhance(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 组装组件key
     *
     * @param field 字段对象
     * @return component 组件
     * @author Mr.Yang
     * @date 2018/12/20 0020
     */
    public static String componentKey(Field field, String component) {
        //获取实体类名
        String clazzName = field.getDeclaringClass().toString()
                .substring(field.getDeclaringClass().toString().lastIndexOf(".") + NUM_ONE);
        String tempKey = field.getName();
        return clazzName.concat("_").concat(
                Character.toUpperCase(tempKey.charAt(0)) + tempKey.substring(1)).concat("_").concat(component);
    }

    /**
     * 判断所有的字符串是否都不为空
     *
     * @param strings 指定字符串数组获或可变参数
     * @return 所有字符串都不为空返回 true, 否则放回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean isAllNotEmpty(String... strings) {
        if (null == strings) {
            return false;
        }
        for (String str : strings) {
            if (isEmpty(str)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断字符串是否为空或空白
     *
     * @param string 指定字符串
     * @return null 或空白字符串返回true, 否则返回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean isBlank(String string) {
        if (null == string) {
            return true;
        }
        for (int i = 0, len = string.length(); i < len; ++i) {
            if (!Character.isWhitespace(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两个字符串是否相同
     *
     * @param a 作为对比的字符串
     * @param b 作为对比的字符串
     * @return 是否相同
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean isEquals(String a, String b) {
        return Objects.equals(a, b);
    }

    /**
     * 判断两个字符串是否不同
     *
     * @param a 作为对比的字符串
     * @param b 作为对比的字符串
     * @return 是否不同
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean notEquals(String a, String b) {
        return !isEquals(a, b);
    }

    /**
     * null 或空串 转 指定的字符串
     *
     * @param str             字符串
     * @param defaultEmptyStr 用于替换 null 或空串的字符串
     * @return 如果字符串不为 null 或空串, 将返回该字符串; 否则返回指定的默认字符串
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String emptyToDefault(String str, String defaultEmptyStr) {
        return null == str || str.length() == 0 ? defaultEmptyStr : str;
    }

    /**
     * 将字符串进行 UTF-8 编码
     *
     * @param string 指定字符串
     * @return 编码后的字符串
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String utf8Encode(String string) {
        if (!isEmpty(string) && string.getBytes().length != string.length()) {
            try {
                return URLEncoder.encode(string, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return string;
    }

    /**
     * 将字符串进行 UTF-8 编码
     *
     * @param string        指定字符串
     * @param defaultReturn 编码失败返回的字符串
     * @return 编码后的字符串
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String utf8Encode(String string, String defaultReturn) {
        if (!isEmpty(string) && string.getBytes().length != string.length()) {
            try {
                return URLEncoder.encode(string, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                return defaultReturn;
            }
        }
        return string;
    }

    /**
     * 判断字符串中是否存在中文汉字
     *
     * @param string 指定字符串
     * @return 是否存在
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean hasChineseChar(String string) {
        boolean temp = false;
        Pattern prule = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = prule.matcher(string);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }


    /**
     * 格式化字符串, 用参数进行替换, 例子: format("I am {arg1}, {arg2}", arg1, arg2);
     *
     * @param format 需要格式化的字符串
     * @param args   格式化参数
     * @return 格式化后的字符串
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String format(String format, Object... args) {
        for (Object arg : args) {
            format = format.replaceFirst("\\{[^\\}]+\\}", arg.toString());
        }
        return format;
    }
}
