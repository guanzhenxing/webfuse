
package cn.webfuse.core.kit;

import cn.webfuse.core.exception.ClassInstantiationException;

/**
 * ClassLoader工具类
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.reflect.ClassLoaderUtil) and made some changes.
 * <p>
 * copy from tiny (org.tinygroup.commons.tools.ClassLoaderUtil) and made some changes.
 */
public class ClassLoaderKits {

    /**
     * 获取当前线程的{@link ClassLoader}
     *
     * @return 当前线程的class loader
     * @see Thread#getContextClassLoader()
     */
    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Copy from Spring, 按顺序获取默认ClassLoader
     * <p>
     * 1. Thread.currentThread().getContextClassLoader()
     * <p>
     * 2. ClassLoaderUtil的加载ClassLoader
     * <p>
     * 3. SystemClassLoader
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) { // NOSONAR
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassLoaderKits.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) { // NOSONAR
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    /**
     * 探测类是否存在classpath中
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            classLoader.loadClass(className);
            return true;
        } catch (Throwable ex) { // NOSONAR
            // Class or one of its dependencies is not present...
            return false;
        }
    }

    // ==========================================================================
    // 装入类的方法。
    // ==========================================================================

    /**
     * 从当前线程的<code>ClassLoader</code>装入类。对于JDK1.2以下，则相当于
     * <code>Class.forName</code>。
     *
     * @param className 要装入的类名
     * @return 已装入的类
     * @throws ClassNotFoundException 如果类没找到
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        return loadClass(className, getContextClassLoader());
    }

    /**
     * 从指定的调用者的<code>ClassLoader</code>装入类。
     *
     * @param className 要装入的类名
     * @param referrer  调用者类，如果为<code>null</code>，则该方法相当于
     *                  <code>Class.forName</code>
     * @return 已装入的类
     * @throws ClassNotFoundException 如果类没找到
     */
    public static Class<?> loadClass(String className, Class<?> referrer) throws ClassNotFoundException {
        ClassLoader classLoader = getReferrerClassLoader(referrer);

        // 如果classLoader为null，表示从ClassLoaderUtil所在的classloader中装载，
        // 这个classloader未必是System class loader。
        return loadClass(className, classLoader);
    }

    /**
     * 从指定的<code>ClassLoader</code>中装入类。如果未指定<code>ClassLoader</code>， 则从装载
     * <code>ClassLoaderUtil</code>的<code>ClassLoader</code>中装入。
     *
     * @param className   要装入的类名
     * @param classLoader 从指定的<code>ClassLoader</code>中装入类，如果为<code>null</code>
     *                    ，表示从<code>ClassLoaderUtil</code>所在的class loader中装载
     * @return 已装入的类
     * @throws ClassNotFoundException 如果类没找到
     */
    public static Class<?> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
        if (className == null) {
            return null;
        }

        if (classLoader == null) {
            return Class.forName(className);
        } else {
            return Class.forName(className, true, classLoader);
        }
    }


    /**
     * 取得调用者的class loader。
     *
     * @param referrer 调用者类
     * @return 调用者的class loader，如果referrer为<code>null</code>，则返回
     * <code>null</code>
     */
    private static ClassLoader getReferrerClassLoader(Class<?> referrer) {
        ClassLoader classLoader = null;

        if (referrer != null) {
            classLoader = referrer.getClassLoader();

            // classLoader为null，说明referrer类是由bootstrap classloader装载的，
            // 例如：java.lang.String
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }

        return classLoader;
    }


    // ==========================================================================
    // 装入并实例化类的方法。
    // ==========================================================================

    /**
     * 从当前线程的<code>ClassLoader</code>装入类并实例化之。
     *
     * @param className 要实例化的类名
     * @return 指定类名的实例
     * @throws ClassNotFoundException      如果类没找到
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newInstance(String className) throws ClassNotFoundException, ClassInstantiationException {
        return newInstance(loadClass(className));
    }

    /**
     * 从指定的调用者的<code>ClassLoader</code>装入类并实例化之。
     *
     * @param className 要实例化的类名
     * @param referrer  调用者类，如果为<code>null</code>，则从<code>ClassLoaderUtil</code>
     *                  所在的class loader装载
     * @return 指定类名的实例
     * @throws ClassNotFoundException      如果类没找到
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newInstance(String className, Class<?> referrer) throws ClassNotFoundException,
            ClassInstantiationException {
        return newInstance(loadClass(className, referrer));
    }

    /**
     * 从指定的<code>ClassLoader</code>中装入类并实例化之。如果未指定<code>ClassLoader</code>， 则从装载
     * <code>ClassLoaderUtil</code>的<code>ClassLoader</code>中装入。
     *
     * @param className   要实例化的类名
     * @param classLoader 从指定的<code>ClassLoader</code>中装入类，如果为<code>null</code>
     *                    ，表示从<code>ClassLoaderUtil</code>所在的class loader中装载
     * @return 指定类名的实例
     * @throws ClassNotFoundException      如果类没找到
     * @throws ClassInstantiationException 如果实例化失败
     */
    public static Object newInstance(String className, ClassLoader classLoader) throws ClassNotFoundException,
            ClassInstantiationException {
        return newInstance(loadClass(className, classLoader));
    }

    /**
     * 创建指定类的实例。
     *
     * @param clazz 要创建实例的类
     * @return 指定类的实例
     * @throws ClassInstantiationException 如果实例化失败
     */
    private static Object newInstance(Class<?> clazz) throws ClassInstantiationException {
        if (clazz == null) {
            return null;
        }

        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new ClassInstantiationException("Failed to instantiate class: " + clazz.getName(), e);
        } catch (IllegalAccessException e) {
            throw new ClassInstantiationException("Failed to instantiate class: " + clazz.getName(), e);
        } catch (Exception e) {
            throw new ClassInstantiationException("Failed to instantiate class: " + clazz.getName(), e);
        }
    }


}
