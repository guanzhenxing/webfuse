package cn.webfuse.core.kit.collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Map的工具集
 */
public class MapKits {
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;


    /**
     * ConcurrentMap的putIfAbsent()返回之前的Value，此函数封装返回最终存储在Map中的Value
     * <p>
     * copy from vjtools
     *
     * @param map
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @return
     * @see org.apache.commons.lang3.concurrent.ConcurrentUtils#putIfAbsent(ConcurrentMap, Object, Object)
     */
    public static <K, V> V putIfAbsentReturnLast(final ConcurrentMap<K, V> map, final K key, final V value) {
        final V result = map.putIfAbsent(key, value);
        return result != null ? result : value;
    }


    /**
     * 如果Key不存在则创建，返回最后存储在Map中的Value.
     * <p>
     * 如果创建Value对象有一定成本, 直接使用PutIfAbsent可能重复浪费，则使用此类，传入一个被回调的ValueCreator，Lazy创建对象。
     * <p>
     * copy from vjtools
     *
     * @param map
     * @param key
     * @param creator
     * @param <K>
     * @param <V>
     * @return
     * @see org.apache.commons.lang3.concurrent.ConcurrentUtils#createIfAbsent(ConcurrentMap, Object,
     * org.apache.commons.lang3.concurrent.ConcurrentInitializer)
     */
    public static <K, V> V createIfAbsentReturnLast(final ConcurrentMap<K, V> map, final K key,
                                                    final ValueCreator<? extends V> creator) {
        final V value = map.get(key);
        if (value == null) {
            return putIfAbsentReturnLast(map, key, creator.get());
        }
        return value;
    }

    /**
     * Lazy创建Value值的回调类
     */
    public interface ValueCreator<T> {
        /**
         * 创建对象
         */
        T get();
    }

    /**
     * 根据等号左边的类型, 构造类型正确的HashMap.
     * <p>
     * 注意HashMap中有0.75的加载因子的影响, 需要进行运算后才能正确初始化HashMap的大小.
     * <p>
     * 加载因子也是HashMap中减少Hash冲突的重要一环，如果读写频繁，总记录数不多的Map，可以比默认值0.75进一步降低，建议0.5
     * <p>
     * copy from vjtools
     *
     * @param expectedSize 期待的Map大小
     * @param loadFactor   加载因子
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> newHashMapWithCapacity(int expectedSize, float loadFactor) {
        int finalSize = (int) (expectedSize / loadFactor + 1.0F);
        return new HashMap<K, V>(finalSize, loadFactor);
    }

    /**
     * 根据等号左边的类型, 构造类型正确的HashMap.
     * <p>
     * 同时初始化第一个元素
     * <p>
     * copy from vjtools
     *
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> newHashMap(final K key, final V value) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(key, value);
        return map;
    }

    /**
     * 根据等号左边的类型, 构造类型正确的HashMap.
     * <p>
     * 同时初始化元素.
     * <p>
     * copy from vjtools
     *
     * @param keys
     * @param values
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> newHashMap(final K[] keys, final V[] values) {
        Validate.isTrue(keys.length == values.length, "keys.length is %d but values.length is %d", keys.length,
                values.length);

        HashMap<K, V> map = new HashMap<K, V>(keys.length * 2);

        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }

        return map;
    }

    /**
     * 根据等号左边的类型, 构造类型正确的HashMap.
     * <p>
     * 同时初始化元素.
     * <p>
     * copy from vjtools
     *
     * @param keys
     * @param values
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> newHashMap(final List<K> keys, final List<V> values) {
        Validate.isTrue(keys.size() == values.size(), "keys.length is %s  but values.length is %s", keys.size(),
                values.size());

        HashMap<K, V> map = new HashMap<K, V>(keys.size() * 2);
        Iterator<K> keyIt = keys.iterator();
        Iterator<V> valueIt = values.iterator();

        while (keyIt.hasNext()) {
            map.put(keyIt.next(), valueIt.next());
        }

        return map;
    }

    /**
     * 根据等号左边的类型，构造类型正确的TreeMap.
     * <p>
     * copy from vjtools
     *
     * @param <K>
     * @param <V>
     * @return
     * @see com.google.common.collect.Maps#newTreeMap()
     */
    public static <K extends Comparable, V> TreeMap<K, V> newSortedMap() {
        return new TreeMap<K, V>();
    }

    /**
     * 根据等号左边的类型，构造类型正确的TreeMap.
     * <p>
     * copy from vjtools
     *
     * @param comparator
     * @param <C>
     * @param <K>
     * @param <V>
     * @return
     * @see com.google.common.collect.Maps#newTreeMap(Comparator)
     */
    public static <C, K extends C, V> TreeMap<K, V> newSortedMap(Comparator<C> comparator) {
        return Maps.newTreeMap(comparator);
    }

    /**
     * 相比HashMap，当key是枚举类时, 性能与空间占用俱佳.
     * <p>
     * copy from vjtools
     *
     * @param type
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
        return new EnumMap<K, V>(Preconditions.checkNotNull(type));
    }

    /**
     * 根据等号左边的类型，构造类型正确的ConcurrentSkipListMap.
     * <p>
     * copy from vjtools
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ConcurrentSkipListMap<K, V> newConcurrentSortedMap() {
        return new ConcurrentSkipListMap<K, V>();
    }

    /**
     * 如果map为null，转化为一个安全的空Map.
     * <p>
     * 注意返回的Map不可写, 写入会抛出UnsupportedOperationException.
     * <p>
     * copy from vjtools
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     * @see java.util.Collections#emptyMap()
     */
    public static <K, V> Map<K, V> emptyMapIfNull(final Map<K, V> map) {
        return map == null ? (Map<K, V>) Collections.EMPTY_MAP : map;
    }

    /**
     * 对两个Map进行比较，返回MapDifference，然后各种妙用.
     * <p>
     * 包括key的差集，key的交集，以及key相同但value不同的元素。
     * <p>
     * copy from vjtools
     *
     * @param left
     * @param right
     * @param <K>
     * @param <V>
     * @return
     * @see com.google.common.collect.MapDifference
     */
    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left,
                                                        Map<? extends K, ? extends V> right) {
        return Maps.difference(left, right);
    }

    //TODO
    public static <V> V getValue(Map<String, Object> map, String key, Class<V> valueType) {


        return null;
    }


}
