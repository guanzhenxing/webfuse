package cn.webfuse.core.kit.id;

/**
 * ID生成器
 *
 * @param <T>
 */
@FunctionalInterface
public interface IdGenerator<T> {

    T generate();

    /**
     * 使用UUID生成id
     */
    IdGenerator<String> UUID = () -> java.util.UUID.randomUUID().toString();

    /**
     * 雪花算法
     */
    IdGenerator<Long> SNOW_FLAKE = SnowflakeGenerator.getInstance()::nextId;

    /**
     * 雪花算法转String
     */
    IdGenerator<String> SNOW_FLAKE_STRING = () -> String.valueOf(SNOW_FLAKE.generate());

    /**
     * 类MongoDB的ObjectId
     */
    IdGenerator<String> OBJECT_ID = ObjectIdGenerator::next;

}
