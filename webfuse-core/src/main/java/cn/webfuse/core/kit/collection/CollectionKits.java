package cn.webfuse.core.kit.collection;


import cn.webfuse.core.lang.Pair;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Collection的工具集
 */
public class CollectionKits {

    /**
     * 取得Collection的第一个元素，如果collection为空返回null.
     * <p>
     * copy from vjtools
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(0);
        }
        return collection.iterator().next();
    }

    /**
     * 获取Collection的最后一个元素，如果collection为空返回null.
     * <p>
     * copy from vjtools
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> T getLast(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }

        // 当类型List时，直接取得最后一个元素.
        if (collection instanceof List) {
            List<T> list = (List<T>) collection;
            return list.get(list.size() - 1);
        }

        return Iterators.getLast(collection.iterator());
    }

    /**
     * 同时返回无序集合中的最小值和最大值，使用元素默认排序
     * <p>
     * 在返回的Pair中，第一个为最小值，第二个为最大值
     * <p>
     * copy from vjtools
     *
     * @param coll
     * @param <T>
     * @return
     */
    public static <T extends Object & Comparable<? super T>> Pair<T, T> minAndMax(Collection<? extends T> coll) {
        Iterator<? extends T> i = coll.iterator();
        T minCandidate = i.next();
        T maxCandidate = minCandidate;

        while (i.hasNext()) {
            T next = i.next();
            if (next.compareTo(minCandidate) < 0) {
                minCandidate = next;
            } else if (next.compareTo(maxCandidate) > 0) {
                maxCandidate = next;
            }
        }
        return Pair.of(minCandidate, maxCandidate);
    }

    /**
     * 返回无序集合中的最小值和最大值
     * <p>
     * 在返回的Pair中，第一个为最小值，第二个为最大值
     * <p>
     * copy from vjtools
     *
     * @param coll
     * @param comp
     * @param <T>
     * @return
     */
    public static <T> Pair<T, T> minAndMax(Collection<? extends T> coll, Comparator<? super T> comp) {

        Iterator<? extends T> i = coll.iterator();
        T minCandidate = i.next();
        T maxCandidate = minCandidate;

        while (i.hasNext()) {
            T next = i.next();
            if (comp.compare(next, minCandidate) < 0) {
                minCandidate = next;
            } else if (comp.compare(next, maxCandidate) > 0) {
                maxCandidate = next;
            }
        }

        return Pair.of(minCandidate, maxCandidate);
    }

    /**
     * 返回Iterable中最大的N个对象.
     * <p>
     * copy from vjtools
     *
     * @param coll
     * @param n
     * @param <T>
     * @return
     */
    public static <T extends Comparable<?>> List<T> topN(Iterable<T> coll, int n) {
        return Ordering.natural().greatestOf(coll, n);
    }

    /**
     * 返回Iterable中最大的N个对象.
     * <p>
     * copy from vjtools
     *
     * @param coll
     * @param n
     * @param comp
     * @param <T>
     * @return
     */
    public static <T> List<T> topN(Iterable<T> coll, int n, Comparator<? super T> comp) {
        return Ordering.from(comp).greatestOf(coll, n);
    }

    /**
     * 返回Iterable中最小的N个对象.
     * <p>
     * copy from vjtools
     *
     * @param coll
     * @param n
     * @param <T>
     * @return
     */
    public static <T extends Comparable<?>> List<T> bottomN(Iterable<T> coll, int n) {
        return Ordering.natural().leastOf(coll, n);
    }

    /**
     * 返回Iterable中最小的N个对象.
     * <p>
     * copy from vjtools
     *
     * @param coll
     * @param n
     * @param comp
     * @param <T>
     * @return
     */
    public static <T> List<T> bottomN(Iterable<T> coll, int n, Comparator<? super T> comp) {
        return Ordering.from(comp).leastOf(coll, n);
    }
}
