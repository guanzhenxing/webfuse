package cn.webfuse.framework.core.kit;

import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * 数组工具类
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.collection.ArrayUtil) and made some changes.
 */
public class ArrayKits {

    /**
     * 传入类型与大小创建数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }

    /**
     * 返回数组某个下标的元素
     */
    public static <T> T getElement(T[] array, int index) {
        return array == null ? null : array.length - 1 < index ? null : array[index];
    }

    /**
     * 从collection转为Array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> col, Class<T> type) {
        return col.toArray((T[]) Array.newInstance(type, 0));
    }

    /**
     * 对数组中第i个和第j个元素对调
     */
    private static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 将传入的数组乱序
     */
    public static <T> T[] shuffle(T[] array) {
        if (array != null && array.length > 1) {
            Random rand = new Random();
            return shuffle(array, rand);
        } else {
            return array;
        }
    }

    /**
     * 将传入的数组乱序
     */
    public static <T> T[] shuffle(T[] array, Random random) {
        if (array != null && array.length > 1 && random != null) {
            for (int i = array.length; i > 1; i--) {
                swap(array, i - 1, random.nextInt(i));
            }
        }
        return array;
    }

    /**
     * 添加元素到数组头
     */
    public static <T> T[] concat(T element, T[] array) {
        return ObjectArrays.concat(element, array);
    }

    /**
     * 添加元素到数组末尾.
     */
    public static <T> T[] concat(T[] array, T element) {
        return ObjectArrays.concat(array, element);
    }

    ////////////////// guava Array 转换为底层为原子类型的List ///////////

    /**
     * 原版将数组转换为List.
     * <p>
     * 注意转换后的List不能写入, 否则抛出UnsupportedOperationException
     */
    public static <T> List<T> asList(T... a) {
        return Arrays.asList(a);
    }

    /**
     * Arrays.asList()的加强版, 返回一个底层为原始类型int的List
     * <p>
     * 与保存Integer相比节约空间，同时只在读取数据时AutoBoxing.
     */
    public static List<Integer> intAsList(int... backingArray) {
        return Ints.asList(backingArray);
    }

    /**
     * Arrays.asList()的加强版, 返回一个底层为原始类型long的List
     * <p>
     * 与保存Long相比节约空间，同时只在读取数据时AutoBoxing.
     */
    public static List<Long> longAsList(long... backingArray) {
        return Longs.asList(backingArray);
    }

    /**
     * Arrays.asList()的加强版, 返回一个底层为原始类型double的List
     * <p>
     * 与保存Double相比节约空间，同时也避免了AutoBoxing.
     */
    public static List<Double> doubleAsList(double... backingArray) {
        return Doubles.asList(backingArray);
    }

}
