package cn.webfuse.core.kit.reflect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * 获取Class信息的工具类
 * <p>
 * copy from vjtools and do some modify
 */
@Slf4j
public class ClassKits {

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    /**
     * https://github.com/linkedin/linkedin-utils/blob/master/org.linkedin.util-core/src/main/java/org/linkedin/util/reflect/ReflectUtils.java
     * <p>
     * The purpose of this method is somewhat to provide a better naming / documentation than the javadoc of
     * <code>Class.isAssignableFrom</code> method.
     *
     * @return <code>true</code> if subclass is a subclass or sub interface of superclass
     * <p>
     * copy from vjtools
     */
    public static boolean isSubClassOrInterfaceOf(Class subclass, Class superclass) {
        return superclass.isAssignableFrom(subclass);
    }

    /**
     * 判断某个值对象是否是某个类对象
     *
     * @param clazz  值对象
     * @param target 对象 如：x.class
     * @return
     */
    public static boolean instanceOf(Class clazz, Class target) {
        if (clazz == null) {
            return false;
        }
        if (clazz == target) {
            return true;
        }
        if (target.isInterface()) {
            for (Class aClass : clazz.getInterfaces()) {
                if (aClass == target) {
                    return true;
                }
            }
        }
        if (clazz.getSuperclass() == target) {
            return true;
        } else {
            if (clazz.isInterface()) {
                for (Class aClass : clazz.getInterfaces()) {
                    if (instanceOf(aClass, target)) {
                        return true;
                    }
                }
            }
            return instanceOf(clazz.getSuperclass(), target);
        }
    }

    /**
     * 获取CGLib处理过后的实体的原Class.
     * <p>
     * copy from vjtools
     */
    public static Class<?> unwrapCglib(Object instance) {
        Validate.notNull(instance, "Instance must not be null");
        Class<?> clazz = instance.getClass();
        if ((clazz != null) && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if ((superClass != null) && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型,
     * <p>
     * 注意泛型必须定义在父类处. 这是唯一可以通过反射从泛型获得Class实例的地方.
     * <p>
     * 如无法找到, 返回Object.class.
     * <p>
     * eg. public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     * <p>
     * copy from vjtools
     */
    public static <T> Class<T> getClassGenericType(final Class clazz) {
        return getClassGenericType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * <p>
     * 注意泛型必须定义在父类处. 这是唯一可以通过反射从泛型获得Class实例的地方.
     * <p>
     * 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic declaration, start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     * <p>
     * copy from vjtools
     */
    public static Class getClassGenericType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if ((index >= params.length) || (index < 0)) {
            log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }
}
