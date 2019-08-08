package cn.webfuse.framework.core.kit;


import cn.webfuse.framework.core.type.collection.SortedArrayList;

import java.util.*;

/**
 * 关于List的工具集合
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.collection.ListUtil) and made some changes.
 */
@SuppressWarnings("unchecked")
public class ListKits {

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(List<?> list) {
        return (list == null) || list.isEmpty();
    }

    /**
     * 判断是否不为空.
     */
    public static boolean isNotEmpty(List<?> list) {
        return (list != null) && !(list.isEmpty());
    }

    /**
     * 获取第一个元素, 如果List为空返回 null.
     */
    public static <T> T getFirst(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取最后一个元素，如果List为空返回null.
     */
    public static <T> T getLast(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }


    /**
     * 如果list为null，转化为一个安全的空List.
     * <p>
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     */
    public static <T> List<T> emptyListIfNull(final List<T> list) {
        return list == null ? (List<T>) Collections.EMPTY_LIST : list;
    }


    ///////////////// 集合运算 ///////////////////

    /**
     * list1,list2的并集（在list1或list2中的对象），产生新List
     */
    public static <E> List<E> union(final List<? extends E> list1, final List<? extends E> list2) {
        final List<E> result = new ArrayList<>(list1.size() + list2.size());
        result.addAll(list1);
        result.addAll(list2);
        return result;
    }

    /**
     * list1, list2的交集（同时在list1和list2的对象），产生新List
     * <p>
     * 与List.retainAll()相比，考虑了的List中相同元素出现的次数, 如"a"在list1出现两次，而在list2中只出现一次，则交集里会保留一个"a".
     */
    public static <T> List<T> intersection(final List<? extends T> list1, final List<? extends T> list2) {
        List<? extends T> smaller = list1;
        List<? extends T> larger = list2;
        if (list1.size() > list2.size()) {
            smaller = list2;
            larger = list1;
        }

        // 克隆一个可修改的副本
        List<T> newSmaller = new ArrayList<T>(smaller);
        List<T> result = new ArrayList<T>(smaller.size());
        for (final T e : larger) {
            if (newSmaller.contains(e)) {
                result.add(e);
                newSmaller.remove(e);
            }
        }
        return result;
    }

    /**
     * list1, list2的差集（在list1，不在list2中的对象），产生新List.
     * <p>
     * 与List.removeAll()相比，会计算元素出现的次数，如"a"在list1出现两次，而在list2中只出现一次，则差集里会保留一个"a".
     */
    public static <T> List<T> difference(final List<? extends T> list1, final List<? extends T> list2) {
        final List<T> result = new ArrayList<T>(list1);
        final Iterator<? extends T> iterator = list2.iterator();

        while (iterator.hasNext()) {
            result.remove(iterator.next());
        }

        return result;
    }

    /**
     * list1, list2的补集（在list1或list2中，但不在交集中的对象，又叫反交集）产生新List.
     * <p>
     * copy from Apache Common Collection4 ListUtils，但其并集－交集时，初始大小没有对交集*2，所以做了修改
     */
    public static <T> List<T> disjoint(final List<? extends T> list1, final List<? extends T> list2) {
        List<T> intersection = intersection(list1, list2);
        List<T> towIntersection = union(intersection, intersection);
        return difference(union(list1, list2), towIntersection);
    }


    /**
     * 排序的ArrayList.
     * <p>
     * from Jodd的新类型，插入时排序，但指定插入index的方法如add(index,element)不支持
     */
    public static <T extends Comparable> SortedArrayList<T> createSortedArrayList() {
        return new SortedArrayList<>();
    }

    /**
     * 排序的ArrayList.
     * <p>
     * from Jodd的新类型，插入时排序，但指定插入index的方法如add(index,element)不支持
     */
    public static <T> SortedArrayList<T> createSortedArrayList(Comparator<? super T> c) {
        return new SortedArrayList<T>(c);
    }
}
