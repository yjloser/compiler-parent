package com.semitransfer.compiler.core.config.internal.api.encrypt;


import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

import static com.semitransfer.compiler.core.config.internal.api.Constants.NUM_TWO;
import static com.semitransfer.compiler.core.config.internal.api.Constants.NUM_ZERO;


/**
 * <p>
 * 加密规则
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class HexUtils {
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    /**
     * 16进制转byte数组
     *
     * @param data 16进制字符串
     * @return byte数组
     * @throws Exception 转化失败的异常
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static byte[] hex2Bytes(final String data) throws Exception {
        final int len = data.length();
        if ((len & 0x01) != 0) {
            throw new Exception("Odd number of characters.");
        }
        final byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data.charAt(j), j) << 4;
            j++;
            f = f | toDigit(data.charAt(j), j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }


    /**
     * bytes数组转16进制String
     *
     * @param data bytes数组
     * @return 转化结果
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String bytes2Hex(final byte[] data) {
        return bytes2Hex(data, true);
    }


    /**
     * bytes数组转16进制String
     *
     * @param data        bytes数组
     * @param toLowerCase 是否小写
     * @return 转化结果
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String bytes2Hex(final byte[] data, final boolean toLowerCase) {
        return bytes2Hex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }


    /**
     * bytes数组转16进制String
     *
     * @param data     bytes数组
     * @param toDigits DIGITS_LOWER或DIGITS_UPPER
     * @return 转化结果
     * @author Mr.Yang
     * @date 2018/7/7
     */
    private static String bytes2Hex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return new String(out);
    }


    /**
     * 16转化为数字
     *
     * @param ch    16进制
     * @param index 索引
     * @return 转化结果
     * @throws Exception 转化失败异常
     * @author Mr.Yang
     * @date 2018/7/7
     */
    private static int toDigit(final char ch, final int index)
            throws Exception {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new Exception("Illegal hexadecimal character " + ch
                    + " at index " + index);
        }
        return digit;
    }

    /**
     * 16进制字符串转字符串
     *
     * @param hex 字符串
     * @return 返回字符串
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String hex2String(String hex) {
        return bytes2String(hexString2Bytes(hex));
    }

    /**
     * 字节数组转字符串
     *
     * @param b byte数组
     * @return 返回字符串
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String bytes2String(byte[] b) {
        return new String(b, StandardCharsets.UTF_8);
    }

    /**
     * 16进制字符串转字节数组
     *
     * @param hex 字符串
     * @return byte[] 返回byte数组
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static byte[] hexString2Bytes(String hex) {
        if (StringUtils.isEmpty(hex)) {
            return null;
        } else if (hex.length() % NUM_TWO != NUM_ZERO) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }
    }

    /**
     * 字符转换为字节
     *
     * @param c chat类型
     * @return 返回byte
     * @author Mr.Yang
     * @date 2018/7/7
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 字符串转16进制字符串
     *
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String string2HexString(String s) {
        return bytes2HexString(string2Bytes(s));
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param b byte数组
     * @return 返回字符串
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static String bytes2HexString(byte[] b) {
        StringBuilder r = new StringBuilder();
        for (byte aB : b) {
            String hex = Integer.toHexString(aB & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r.append(hex.toUpperCase());
        }
        return r.toString();
    }

    /**
     * 字符串转字节数组
     *
     * @param s 字符串
     * @return byte[] 返回byte数组
     * @author Mr.Yang
     * @date 2018/7/7
     */
    public static byte[] string2Bytes(String s) {
        return s.getBytes();
    }
}
