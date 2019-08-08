package cn.webfuse.framework.model;

import cn.webfuse.framework.core.kit.mapper.BeanMapper;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO(Data Transfer Object):数据传输对象，Service 或 Manager 向外传输的对象。
 */
public abstract class AbstractBaseDTO implements Serializable {


    public Map<String, Object> toMap(boolean ignoreParent, boolean ignoreEmptyValue, String... ignoreProperties) {
        return BeanMapper.convertBeanToMap(this, ignoreParent, ignoreEmptyValue, ignoreProperties);
    }

    public Map<String, Object> toMap() {
        return toMap(false, false, new String[0]);
    }

    public <E extends AbstractBaseDTO> E fromMap(Map<String, Object> map) {
        return (E) BeanMapper.convertMapToBean(map, AbstractBaseDTO.class);
    }

    public <E extends AbstractBaseDO> E toDo() {
        return (E) BeanMapper.map(this, AbstractBaseDO.class);
    }

    public <E extends AbstractBaseDTO> E fromDto(AbstractBaseDO baseDo) {
        return (E) BeanMapper.map(baseDo, this.getClass());
    }

}
