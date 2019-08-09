package cn.webfuse.core.kit;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

/**
 * 当lambda中遇到try-catch的时候可以使用这个进行
 * https://segmentfault.com/a/1190000007832130
 */
@Slf4j
public class TryCatchKits {

    /**
     * 将会抛出异常的函数进行包装，使其不抛出受检异常
     *
     * @param mapper
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return t -> {
            try {
                return mapper.apply(t);
            } catch (Throwable e) {
                throw ExceptionKits.unchecked(e);
            }
        };
    }

    /**
     * 将会抛出异常的函数进行包装，使其不抛出受检异常。发送异常时返回默认值
     *
     * @param mapper
     * @param defaultR
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper, R defaultR) {
        Objects.requireNonNull(mapper);
        return t -> {
            try {
                return mapper.apply(t);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                return defaultR;
            }
        };
    }
    
    @FunctionalInterface
    public interface UncheckedFunction<T, R> {
        /**
         * Run the Consumer
         *
         * @param t T
         * @return R R
         * @throws Throwable UncheckedException
         */
        @Nullable
        R apply(@Nullable T t) throws Throwable;
    }

}
