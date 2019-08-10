package cn.webfuse.framework.kit;

import cn.webfuse.core.kit.ExceptionKits;
import cn.webfuse.core.kit.ReflectionKits;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Aspectj工具类
 */
public class AspectjKits {

    public static Class<?> getClass(JoinPoint joinPoint) {
        try {
            String classType = joinPoint.getTarget().getClass().getName();
            return Class.forName(classType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw ExceptionKits.unchecked(e);
        }
    }

    public static Method getMethod(JoinPoint joinPoint) {
        Class target = getClass(joinPoint);
        String methodName = joinPoint.getSignature().getName();
        Class[] parameterTypes = getParameterTypes(joinPoint);
        Method method = ReflectionKits.getMethod(target, methodName, parameterTypes);
        return method;
    }

    public static Class[] getParameterTypes(JoinPoint joinPoint) {
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        return parameterTypes;
    }

    public static Object[] getArgs(JoinPoint joinPoint) {
        return joinPoint.getArgs();
    }

    public static Map<String, Object> getArgsMap(JoinPoint joinPoint) {

        Method method = getMethod(joinPoint);
        Object[] args = getArgs(joinPoint);

        Map<String, Object> argMap = new LinkedHashMap<>();
        String[] argNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        for (int i = 0, len = args.length; i < len; i++) {
            argMap.put((argNames == null || argNames[i] == null) ? "arg" + i : argNames[i], args[i]);
        }
        return argMap;
    }


}
