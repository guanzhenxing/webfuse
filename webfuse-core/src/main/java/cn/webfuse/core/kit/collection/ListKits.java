package cn.webfuse.core.kit.collection;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * List的工具集
 */
public class ListKits {

    /**
     * 获取第一个元素, 如果List为空返回 null.
     * <p>
     * copy from  vjtools
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T getFirst(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取最后一个元素，如果List为空返回null.
     * <p>
     * copy from  vjtools
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> T getLast(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * 如果list为null，转化为一个安全的空List.
     * <p>
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     * <p>
     * copy from  vjtools
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> emptyListIfNull(final List<T> list) {
        return list == null ? (List<T>) Collections.EMPTY_LIST : list;
    }

    /**
     * 返回只含一个元素但结构特殊的List，节约空间.
     * <p>
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     * <p>
     * copy from  vjtools
     *
     * @param o
     * @param <T>
     * @return
     */
    public static <T> List<T> singletonList(T o) {
        return Collections.singletonList(o);
    }

    /**
     * 返回包装后不可修改的List.
     * <p>
     * 如果尝试写入会抛出UnsupportedOperationException.
     * <p>
     * copy from  vjtools
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> unmodifiableList(List<? extends T> list) {
        return Collections.unmodifiableList(list);
    }

    /**
     * 返回包装后同步的List，所有方法都会被synchronized原语同步.
     * <p>
     * 用于CopyOnWriteArrayList与 ArrayDequeue均不符合的场景
     * <p>
     * copy from  vjtools
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> synchronizedList(List<T> list) {
        return Collections.synchronizedList(list);
    }

    /**
     * 清理掉List中的Null对象
     * <p>
     * copy from  vjtools
     *
     * @param list
     * @param <T>
     */
    public static <T> void notNullList(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        Iterator<T> ite = list.iterator();
        while (ite.hasNext()) {
            T obj = ite.next();
            // 清理掉null的集合
            if (null == obj) {
                ite.remove();
            }
        }
    }

    /**
     * 清理掉List中的Null对象，并唯一化
     * <p>
     * copy from  vjtools
     *
     * @param list
     * @param <T>
     */
    public static <T> void uniqueNotNullList(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Iterator<T> ite = list.iterator();
        Set<T> set = new HashSet<>((int) (list.size() / 0.75F + 1.0F));

        while (ite.hasNext()) {
            T obj = ite.next();
            // 清理掉null的集合
            if (null == obj) {
                ite.remove();
                continue;
            }
            // 清理掉重复的集合
            if (set.contains(obj)) {
                ite.remove();
                continue;
            }
            set.add(obj);
        }
    }

    /**
     * list1,list2的并集（在list1或list2中的对象），产生新List
     * <p>
     * 对比Apache Common Collection4 ListUtils, 优化了初始大小
     * <p>
     * copy from  vjtools
     *
     * @param list1
     * @param list2
     * @param <E>
     * @return
     */
    public static <E> List<E> union(final List<? extends E> list1, final List<? extends E> list2) {
        final List<E> result = new ArrayList<E>(list1.size() + list2.size());
        result.addAll(list1);
        result.addAll(list2);
        return result;
    }

    /**
     * list1, list2的交集（同时在list1和list2的对象），产生新List
     * <p>
     * copy from Apache Common Collection4 ListUtils，但其做了不合理的去重，因此重新改为性能较低但不去重的版本
     * <p>
     * 与List.retainAll()相比，考虑了的List中相同元素出现的次数, 如"a"在list1出现两次，而在list2中只出现一次，则交集里会保留一个"a".
     * <p>
     * copy from  vjtools
     *
     * @param list1
     * @param list2
     * @param <T>
     * @return
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
     * <p>
     * copy from  vjtools
     *
     * @param list1
     * @param list2
     * @param <T>
     * @return
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
     * copy from  vjtools
     *
     * @param list1
     * @param list2
     * @param <T>
     * @return
     */
    public static <T> List<T> disjoint(final List<? extends T> list1, final List<? extends T> list2) {
        List<T> intersection = intersection(list1, list2);
        List<T> towIntersection = union(intersection, intersection);
        return difference(union(list1, list2), towIntersection);
    }


}
