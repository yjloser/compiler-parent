package com.semitransfer.compiler.core.config.internal.api.util;

import java.lang.reflect.Array;


/**
 * <p>
 * 数组操作
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class ArrayUtils {

    private ArrayUtils() {
    }

    /**
     * 判断数组是否为空
     *
     * @param array 指定的数组
     * @return null 或空数组返回 true; 不为空返回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static <T> boolean isEmpty(T[] array) {
        return null == array || array.length == 0;
    }

    /**
     * 判断是否所有的数组都为空, 单个数组是否为空见 {@link #isEmpty}
     *
     * @param arrays 包含多个数组的数组或可变参数
     * @return 所有数组都为空时返回 true; 任意一个数组不为空时返回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    @SafeVarargs
    public static <T> boolean isAllEmpty(T[]... arrays) {
        for (T[] arr : arrays) {
            if (!isEmpty(arr)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否任意一个数组为空, 单个数组是否为空见 {@link #isEmpty}
     *
     * @param arrays 包含多个数组的数组或可变参数
     * @return 任意一个数组为空时返回 true; 其他(所有数组不为空)返回 false
     * @author Mr.Yang
     * @date 2018/12/1
     */
    @SafeVarargs
    public static <T> boolean isAnyOneEmpty(T[]... arrays) {
        for (T[] arr : arrays) {
            if (isEmpty(arr)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 拼接数组中的所有元素,默认使用逗号隔开
     *
     * @param arr 指定的数组
     * @return 拼接好的字符串
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String joint(Object[] arr) {
        return joint(arr, ",");
    }

    /**
     * 拼接数组中的所有元素
     *
     * @param arr     指定的数组
     * @param divider 串联在元素之间的字符串
     * @return 拼接好的字符串
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String joint(Object[] arr, String divider) {
        if (null == arr) {
            throw new NullPointerException("arr can not be null");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) {
                builder.append(divider);
            }
            builder.append(arr[i].toString());
        }
        return builder.toString();
    }

    /**
     * 拼接数组中的所有元素,默认使用逗号隔开
     *
     * @param arr    指定的数组
     * @param length 需要拼接的元素个数
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String joint(Object[] arr, int length) {
        return joint(arr, length, ",");
    }

    /**
     * 拼接数组中的元素
     *
     * @param arr     指定的数组
     * @param length  需要拼接的元素个数
     * @param divider 串联在元素之间的字符串
     * @return 拼接好的字符串
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String joint(Object[] arr, int length, String divider) {
        if (null == arr) {
            throw new NullPointerException("arr can not be null");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length must be positive");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Math.min(arr.length, length); i++) {
            if (i != 0) {
                builder.append(divider);
            }
            builder.append(arr[i].toString());
        }
        return builder.toString();
    }

    /**
     * 将源数组的元素复制目标数组中<br/>
     * 如果源数组比目标数组长, 源数组超出部分的元素将不被复制;
     * 如果目标数组比源数组长, 目标数组超出部分的元素将不会被改变
     *
     * @param srcArray  源数组; 为 null 将抛出 IllegalArgumentException 异常
     * @param destArray 目标数组; wei null 将抛出 IllegalArgumentException 异常
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static void copy(int[] srcArray, int[] destArray) {
        if (null == srcArray) {
            throw new IllegalArgumentException("srcArray can't be null");
        }
        if (null == destArray) {
            throw new IllegalArgumentException("destArray can't be null");
        }
        if (srcArray.length == 0 || destArray.length == 0) {
            return;
        }
        System.arraycopy(srcArray, 0, destArray, 0, Math.min(srcArray.length, destArray.length));
    }

    /**
     * 获取数组备份
     *
     * @param srcArray 源数组
     * @return 备份后的数组
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static int[] getCopy(int[] srcArray) {
        if (null == srcArray) {
            return null;
        }
        return getCopy(srcArray, srcArray.length);
    }

    /**
     * 获取数组备份
     *
     * @param srcArray  源数组
     * @param newLength 需要备份的长度
     * @return 备份后的数组
     */
    public static int[] getCopy(int[] srcArray, int newLength) {
        int[] destArray = new int[newLength];
        if (srcArray != null && srcArray.length > 0 && newLength > 0) {
            System.arraycopy(srcArray, 0, destArray, 0, Math.min(srcArray.length, destArray.length));
        }
        return destArray;
    }

    /**
     * 合并多个源数组到一个数组中
     *
     * @param clazz     数组类型对应的类, 用于自动创建新数组
     * @param srcArrays 源数组
     * @author Mr.Yang
     * @date 2018/12/1
     */
    @SafeVarargs
    public static <T> T[] mergeArrays(Class<T> clazz, T[]... srcArrays) {
        if (null == srcArrays) {
            return null;
        }
        int length = 0;
        for (T[] srcArray : srcArrays) {
            length += srcArray.length;
        }
        T[] destArray = (T[]) Array.newInstance(clazz, length);
        if (length > 0) {
            int index = 0;
            for (T[] oldArray : srcArrays) {
                for (T t : oldArray) {
                    destArray[index++] = t;
                }
            }
        }
        return destArray;
    }
}
