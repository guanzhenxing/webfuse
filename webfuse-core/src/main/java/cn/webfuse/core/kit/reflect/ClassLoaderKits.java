package cn.webfuse.core.kit.reflect;

/**
 * ClassLoader工具类
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.reflect.ClassLoaderUtil) and made some changes.
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


}
