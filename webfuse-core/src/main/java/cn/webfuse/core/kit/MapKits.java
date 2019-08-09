package cn.webfuse.core.kit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Supplier;

/**
 * 关于Map的工具集合
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.collection.MapUtil) and made some changes.
 */
@SuppressWarnings("unchecked")
public class MapKits {

    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 判断是否为空
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return (map == null) || map.isEmpty();
    }

    /**
     * 判断是否为空
     */
    public static boolean isNotEmpty(final Map<?, ?> map) {
        return (map != null) && !map.isEmpty();
    }


    public static <K, V> Map<K, V> merge(Map<K, V> map, Map<K, V> maps) {
        return merge(new HashMap<>(), map, maps);
    }

    public static <K, V> Map<K, V> merge(Map<K, V> target, Map<K, V> map, Map<K, V>... maps) {
        target.putAll(map);
        for (int i = 0; i < maps.length; i++) {
            target.putAll(maps[i]);
        }
        return target;
    }

    public static <K, V> Map<K, V> merge(Supplier<Map<K, V>> supplier, Map<K, V> map, Map<K, V>... maps) {
        return merge(supplier.get(), map, maps);
    }

    public static Map<String, Object> removeNullValue(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<>();
        if (map == null)
            return newMap;
        map.entrySet().stream().filter(entry -> !Objects.isNull(entry.getValue())).forEach(entry -> {
            newMap.put(entry.getKey(), entry.getValue());
        });
        return newMap;
    }


    /**
     * 根据等号左边的类型, 构造类型正确的HashMap.
     * <p>
     * 注意HashMap中有0.75的加载因子的影响, 需要进行运算后才能正确初始化HashMap的大小.
     * <p>
     * 加载因子也是HashMap中减少Hash冲突的重要一环，如果读写频繁，总记录数不多的Map，可以比默认值0.75进一步降低，建议0.5
     */
    public static <K, V> HashMap<K, V> newHashMapWithCapacity(int expectedSize, float loadFactor) {
        int finalSize = (int) (expectedSize / loadFactor + 1.0F);
        return new HashMap<>(finalSize, loadFactor);
    }

    /**
     * 构造HashMap，同时初始化第一个元素
     */
    public static <K, V> HashMap<K, V> newHashMap(final K key, final V value) {
        HashMap<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    /**
     * 构造HashMap，同时初始化元素
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
     * 构造HashMap，同时初始化元素
     */
    public static <K, V> HashMap<K, V> newHashMap(final List<K> keys, final List<V> values) {
        Validate.isTrue(keys.size() == values.size(), "keys.length is %s  but values.length is %s", keys.size(),
                values.size());

        HashMap<K, V> map = new HashMap<>(keys.size() * 2);
        Iterator<K> keyIt = keys.iterator();
        Iterator<V> valueIt = values.iterator();

        while (keyIt.hasNext()) {
            map.put(keyIt.next(), valueIt.next());
        }

        return map;
    }

    /**
     * 构造类型正确的TreeMap.
     *
     * @see com.google.common.collect.Maps#newTreeMap()
     */
    @SuppressWarnings("rawtypes")
    public static <K extends Comparable, V> TreeMap<K, V> newSortedMap() {
        return Maps.newTreeMap();
    }

    /**
     * 构造类型正确的TreeMap.
     *
     * @see com.google.common.collect.Maps#newTreeMap(Comparator)
     */
    public static <C, K extends C, V> TreeMap<K, V> newSortedMap(@Nullable Comparator<C> comparator) {
        return Maps.newTreeMap(comparator);
    }

    /**
     * 相比HashMap，当key是枚举类时, 性能与空间占用俱佳.
     */
    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
        return new EnumMap<>(Preconditions.checkNotNull(type));
    }


    /**
     * 根据等号左边的类型，构造类型正确的ConcurrentHashMap.
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 根据等号左边的类型，构造类型正确的ConcurrentSkipListMap.
     */
    public static <K, V> ConcurrentSkipListMap<K, V> newConcurrentSortedMap() {
        return new ConcurrentSkipListMap<>();
    }


    /**
     * 如果map为null，转化为一个安全的空Map.
     * <p>
     * 注意返回的Map不可写, 写入会抛出UnsupportedOperationException.
     */
    public static <K, V> Map<K, V> emptyMapIfNull(final Map<K, V> map) {
        return map == null ? (Map<K, V>) Collections.EMPTY_MAP : map;
    }


    //////////// 按值排序及取TOP N的操作 /////////

    /**
     * 对一个Map按Value进行排序，返回排序LinkedHashMap，多用于Value是Counter的情况.
     *
     * @param reverse 按Value的倒序 or 正序排列
     */
    public static <K, V extends Comparable> Map<K, V> sortByValue(Map<K, V> map, final boolean reverse) {
        return sortByValueInternal(map, reverse ? Ordering.from(new ComparableEntryValueComparator<K, V>()).reverse()
                : new ComparableEntryValueComparator<>());
    }

    /**
     * 对一个Map按Value进行排序，返回排序LinkedHashMap.
     */
    public static <K, V> Map<K, V> sortByValue(Map<K, V> map, final Comparator<? super V> comparator) {
        return sortByValueInternal(map, new EntryValueComparator<>(comparator));
    }

    private static <K, V> Map<K, V> sortByValueInternal(Map<K, V> map, Comparator<Entry<K, V>> comparator) {
        Set<Entry<K, V>> entrySet = map.entrySet();
        Entry<K, V>[] entryArray = entrySet.toArray(new Entry[0]);

        Arrays.sort(entryArray, comparator);

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Entry<K, V> entry : entryArray) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 对一个Map按Value进行排序，返回排序LinkedHashMap，最多只返回n条，多用于Value是Counter的情况.
     *
     * @param reverse 按Value的倒序 or 正序排列
     */
    public static <K, V extends Comparable> Map<K, V> topNByValue(Map<K, V> map, final boolean reverse, int n) {
        return topNByValueInternal(map, n, reverse ? Ordering.from(new ComparableEntryValueComparator<K, V>()).reverse()
                : new ComparableEntryValueComparator<>());
    }

    /**
     * 对一个Map按Value进行排序，返回排序LinkedHashMap, 最多只返回n条，多用于Value是Counter的情况.
     */
    public static <K, V> Map<K, V> topNByValue(Map<K, V> map, final Comparator<? super V> comparator, int n) {
        return topNByValueInternal(map, n, new EntryValueComparator<>(comparator));
    }

    private static <K, V> Map<K, V> topNByValueInternal(Map<K, V> map, int n, Comparator<Entry<K, V>> comparator) {
        Entry<K, V>[] entryArray = map.entrySet().toArray(new Entry[0]);
        Arrays.sort(entryArray, comparator);

        Map<K, V> result = new LinkedHashMap<>();
        int size = Math.min(n, entryArray.length);
        for (int i = 0; i < size; i++) {
            Entry<K, V> entry = entryArray[i];
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private static final class ComparableEntryValueComparator<K, V extends Comparable>
            implements Comparator<Entry<K, V>> {
        @Override
        public int compare(Entry<K, V> o1, Entry<K, V> o2) {
            return (o1.getValue()).compareTo(o2.getValue());
        }
    }

    private static final class EntryValueComparator<K, V> implements Comparator<Entry<K, V>> {
        private final Comparator<? super V> comparator;

        private EntryValueComparator(Comparator<? super V> comparator2) {
            this.comparator = comparator2;
        }

        @Override
        public int compare(Entry<K, V> o1, Entry<K, V> o2) {
            return comparator.compare(o1.getValue(), o2.getValue());
        }
    }


}
