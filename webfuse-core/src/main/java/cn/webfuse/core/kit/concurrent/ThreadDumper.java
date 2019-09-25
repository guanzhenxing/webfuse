package cn.webfuse.core.kit.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * copy from vipshop VJTools
 * <p>
 * 由程序触发的ThreadDump，打印到日志中.
 * <p>
 * 因为ThreadDump本身会造成JVM停顿，所以加上了开关和最少间隔时间的选项(默认不限制)
 * <p>
 * 因为ThreadInfo的toString()最多只会打印8层的StackTrace，所以加上了最大打印层数的选项.(默认为8)
 */
public class ThreadDumper {

    private static final int DEFAULT_MAX_STACK_LEVEL = 8;

    private static final int DEFAULT_MIN_INTERVAL = 1000 * 60 * 10; // 10分钟

    private static Logger logger = LoggerFactory.getLogger(ThreadDumper.class);

    private boolean enable = true; // 快速关闭该功能

    private int maxStackLevel; // 打印StackTrace的最大深度

    private TimeIntervalLimiter timeIntervalLimiter;

    public ThreadDumper() {
        this(DEFAULT_MIN_INTERVAL, DEFAULT_MAX_STACK_LEVEL);
    }

    public ThreadDumper(long leastIntervalMills, int maxStackLevel) {
        this.maxStackLevel = maxStackLevel;

        timeIntervalLimiter = new TimeIntervalLimiter(leastIntervalMills, TimeUnit.MILLISECONDS);
    }

    /**
     * 符合条件则打印线程栈.
     */
    public void threadDumpIfNeed() {
        threadDumpIfNeed(null);
    }

    /**
     * 符合条件则打印线程栈.
     *
     * @param reasonMsg 发生ThreadDump的原因
     */
    public void threadDumpIfNeed(String reasonMsg) {
        if (!enable) {
            return;
        }

        if (!timeIntervalLimiter.tryAcquire()) {
            return;
        }

        logger.info("Thread dump by ThreadDumpper" + (reasonMsg != null ? (" for " + reasonMsg) : ""));

        Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
        // 两条日志间的时间间隔，是VM被thread dump堵塞的时间.
        logger.info("Finish the threads snapshot");

        StringBuilder sb = new StringBuilder(8192 * 20).append('\n');

        for (Entry<Thread, StackTraceElement[]> entry : threads.entrySet()) {
            dumpThreadInfo(entry.getKey(), entry.getValue(), sb);
        }
        logger.info(sb.toString());

    }

    /**
     * 打印全部的stack，重新实现threadInfo的toString()函数，因为默认最多只打印8层的stack. 同时，不再打印lockedMonitors和lockedSynchronizers.
     */
    private String dumpThreadInfo(Thread thread, StackTraceElement[] stackTrace, StringBuilder sb) {
        sb.append('\"').append(thread.getName()).append("\" Id=").append(thread.getId()).append(' ')
                .append(thread.getState());
        sb.append('\n');
        int i = 0;
        for (; i < Math.min(maxStackLevel, stackTrace.length); i++) {
            StackTraceElement ste = stackTrace[i];
            sb.append("\tat ").append(ste).append('\n');
        }
        if (i < stackTrace.length) {
            sb.append("\t...").append('\n');
        }

        sb.append('\n');
        return sb.toString();
    }

    /**
     * 快速关闭打印
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 打印ThreadDump的最小时间间隔，单位为秒，默认为0不限制.
     */
    public void setLeastInterval(int leastIntervalSeconds) {
        this.timeIntervalLimiter = new TimeIntervalLimiter(leastIntervalSeconds, TimeUnit.MILLISECONDS);
    }

    /**
     * 打印StackTrace的最大深度, 默认为8
     */
    public void setMaxStackLevel(int maxStackLevel) {
        this.maxStackLevel = maxStackLevel;
    }
}
