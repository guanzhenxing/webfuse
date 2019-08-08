package cn.webfuse.framework.core.kit.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * From: https://github.com/twitter/snowflake
 * <p>
 * 基于snowflake的ID生成器
 * <p>
 * An object that generates IDs.
 * This is broken into a separate class in case we ever want to context multiple worker threads per process
 *
 * @author zhujuan
 */

public class SnowflakeGenerator {
    protected static final Logger LOG = LoggerFactory.getLogger(SnowflakeGenerator.class);

    private long workerId;
    private long dataCenterId;
    private long sequence = 0L;

    private long twepoch = 1288834974657L;

    private long workerIdBits = 5L;
    private long dataCenterIdBits = 5L;
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);
    private long sequenceBits = 12L;

    private long workerIdShift = sequenceBits;
    private long dataCenterIdShift = sequenceBits + workerIdBits;
    private long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long lastTimestamp = -1L;

    private volatile static SnowflakeGenerator singleton = null;

    public static SnowflakeGenerator getInstance() {
        if (singleton == null) {
            synchronized (SnowflakeGenerator.class) {
                if (singleton == null) {
                    Random random = new Random();
                    long workerId = Long.getLong("id-worker", random.nextInt(31));
                    long dataCenterId = Long.getLong("id-dataCenter", random.nextInt(31));
                    singleton = new SnowflakeGenerator(workerId, dataCenterId);
                }
            }
        }
        return singleton;
    }

    public SnowflakeGenerator(long workerId, long dataCenterId) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenter Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        LOG.info(String.format("worker starting. timestamp left shift %d, dataCenter id bits %d, worker id bits %d, sequence bits %d, workerId %d", timestampLeftShift, dataCenterIdBits, workerIdBits, sequenceBits, workerId));
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            LOG.error(String.format("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp));
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (dataCenterId << dataCenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

//    public static void main(String[] args) {
//        System.out.println(new SnowflakeGenerator(0, 0).nextId());
//    }

}
