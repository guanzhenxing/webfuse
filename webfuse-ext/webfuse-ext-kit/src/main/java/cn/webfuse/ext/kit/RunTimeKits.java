package cn.webfuse.ext.kit;

import cn.webfuse.core.constant.StringPool;
import cn.webfuse.core.kit.NumberKits;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.List;

/**
 * 运行时工具
 */
public class RunTimeKits {

    private static volatile int pid = -1;
    private static final int CPU_NUMBER = Runtime.getRuntime().availableProcessors();

    /**
     * 获得当前进程的PID
     * <p>
     * 当失败时返回-1
     *
     * @return pid
     */
    public static int getPid() {
        if (pid > 0) {
            return pid;
        }
        // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf('@');
        if (index > 0) {
            pid = NumberKits.parseNumber(jvmName.substring(0, index), Integer.class, -1);
            return pid;
        }
        return -1;
    }

    /**
     * 返回应用启动到现在的时间
     *
     * @return {Duration}
     */
    public static Duration getUpTime() {
        long upTime = ManagementFactory.getRuntimeMXBean().getUptime();
        return Duration.ofMillis(upTime);
    }

    /**
     * 返回输入的JVM参数列表
     *
     * @return jvm参数
     */
    public static String getJvmArguments() {
        List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        return StringUtils.join(vmArguments, StringPool.SPACE);
    }

    /**
     * 获取CPU核数
     *
     * @return cpu count
     */
    public static int getCpuNumber() {
        return CPU_NUMBER;
    }
}
