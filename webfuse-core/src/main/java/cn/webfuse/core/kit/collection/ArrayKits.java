package cn.webfuse.core.kit.collection;

import com.google.common.collect.ObjectArrays;

import java.lang.reflect.Array;

/**
 * 数据组工具集
 */
public class ArrayKits {

    /**
     * copy from vjtools
     * 传入类型与大小创建数组.
     * <p>
     * Array.newInstance()的性能并不差
     */
    public static <T> T[] newArray(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }


    /**
     * 添加元素到数组头.
     *
     * @param element
     * @param array
     * @param <T>
     * @return
     */
    public static <T> T[] concat(T element, T[] array) {
        return ObjectArrays.concat(element, array);
    }

    /**
     * 添加元素到数组末尾.
     *
     * @param array
     * @param element
     * @param <T>
     * @return
     */
    public static <T> T[] concat(T[] array, T element) {
        return ObjectArrays.concat(array, element);
    }


}
