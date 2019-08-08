package cn.webfuse.framework.context;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局上下文的线程保存
 *
 * @author Jesen
 */
@Slf4j
public class GlobalThreadLocalHolder {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL_CONTEXTS = new NamedThreadLocal<>("GLOBAL_CONTEXT");

    /**
     * 获得先有的上下文
     *
     * @return
     */
    private static Map<String, Object> currentContextMap() {
        Map<String, Object> context = THREAD_LOCAL_CONTEXTS.get();
        if (context == null) {
            context = initContextMap();
        }
        return context;
    }

    /**
     * 初始化上下文
     *
     * @return
     */
    private synchronized static Map<String, Object> initContextMap() {
        Map<String, Object> context = new ConcurrentHashMap<>();
        THREAD_LOCAL_CONTEXTS.set(context);
        return context;
    }

    /**
     * 判断是否已存在对应的Key
     *
     * @param key
     * @return
     */
    public static boolean has(String key) {
        return currentContextMap().containsKey(key);
    }

    /**
     * 获得对应的值
     *
     * @param key
     * @param valueClass
     * @param <T>
     * @return
     */
    public static <T> T get(String key, Class<T> valueClass) {
        Map<String, Object> currentContextMap = currentContextMap();
        if (currentContextMap.containsKey(key)) {
            Object o = currentContextMap.get(key);
            if (valueClass.isInstance(o)) {
                return valueClass.cast(o);
            }
        }
        return null;
    }

    /**
     * 设置对应的值
     *
     * @param key
     * @param value
     * @param <T>
     */
    public static <T> void set(String key, T value) {
        log.debug("set {}:{}", key, value);
        currentContextMap().put(key, value);
    }

    /**
     * 删除对应的值
     *
     * @param key
     */
    public static void delete(String key) {
        Map<String, Object> currentContextMap = currentContextMap();
        if (currentContextMap.containsKey(key)) {
            currentContextMap.remove(key);
        }
    }

    /**
     * 删除全局持有
     */
    public static void removeCurrentContextMap() {
        THREAD_LOCAL_CONTEXTS.remove();
    }

}
