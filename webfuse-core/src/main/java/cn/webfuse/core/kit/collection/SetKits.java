package cn.webfuse.core.kit.collection;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Set的工具集
 */
public class SetKits {

    /**
     * 如果set为null，转化为一个安全的空Set.
     * <p>
     * 注意返回的Set不可写, 写入会抛出UnsupportedOperationException.
     * <p>
     * copy from vjtools
     *
     * @param set
     * @param <T>
     * @return
     * @see java.util.Collections#emptySet()
     */
    public static <T> Set<T> emptySetIfNull(final Set<T> set) {
        return set == null ? (Set<T>) Collections.EMPTY_SET : set;
    }

    /**
     * 从Map构造Set的大杀器, 可以用来制造各种Set
     * <p>
     * copy from vjtools
     *
     * @param map
     * @param <T>
     * @return
     * @see java.util.Collections#newSetFromMap(Map)
     */
    public static <T> Set<T> newSetFromMap(Map<T, Boolean> map) {
        return Collections.newSetFromMap(map);
    }

    /**
     * set1, set2的并集（在set1或set2的对象）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     * <p>
     * copy from vjtools
     *
     * @param set1
     * @param set2
     * @param <E>
     * @return
     */
    public static <E> Set<E> unionView(final Set<? extends E> set1, final Set<? extends E> set2) {
        return Sets.union(set1, set2);
    }

    /**
     * set1, set2的交集（同时在set1和set2的对象）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     * <p>
     * copy from vjtools
     *
     * @param set1
     * @param set2
     * @param <E>
     * @return
     */
    public static <E> Set<E> intersectionView(final Set<E> set1, final Set<?> set2) {
        return Sets.intersection(set1, set2);
    }

    /**
     * set1, set2的差集（在set1，不在set2中的对象）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     * <p>
     * copy from vjtools
     *
     * @param set1
     * @param set2
     * @param <E>
     * @return
     */
    public static <E> Set<E> differenceView(final Set<E> set1, final Set<?> set2) {
        return Sets.difference(set1, set2);
    }

    /**
     * set1, set2的补集（在set1或set2中，但不在交集中的对象，又叫反交集）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     * <p>
     * copy from vjtools
     *
     * @param set1
     * @param set2
     * @param <E>
     * @return
     */
    public static <E> Set<E> disjointView(final Set<? extends E> set1, final Set<? extends E> set2) {
        return Sets.symmetricDifference(set1, set2);
    }
}
