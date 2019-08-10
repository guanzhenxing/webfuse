package cn.webfuse.framework.model;

import cn.webfuse.core.kit.mapper.BeanMapper;
import cn.webfuse.core.kit.mapper.JsonMapper;
import cn.webfuse.core.kit.ReflectionKits;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * VO(View Object):显示层对象，通常是 Web 向模板渲染引擎层传输的对象。
 */
public abstract class AbstractBaseVO implements Serializable {

    public <E extends AbstractBaseDTO> E toDto() {
        return (E) BeanMapper.map(this, AbstractBaseDTO.class);
    }

    public <E extends AbstractBaseVO> E fromDto(AbstractBaseDTO dto) {
        return (E) BeanMapper.map(dto, this.getClass());
    }

    public <E extends AbstractBaseVO> E fromMap(Map<String, Object> map) {
        return (E) BeanMapper.convertMapToBean(map, AbstractBaseVO.class);
    }

    public Map<String, Object> toMap(boolean ignoreParent, boolean ignoreEmptyValue, String... ignoreProperties) {
        return BeanMapper.convertBeanToMap(this, ignoreParent, ignoreEmptyValue, ignoreProperties);
    }

    public Map<String, Object> toMap() {
        return toMap(false, false, new String[0]);
    }

    public String toJson() {
        return JsonMapper.defaultMapper().toJson(this);
    }

    public <E extends AbstractBaseVO> E fromJson(String json) {
        return (E) JsonMapper.defaultMapper().fromJson(json, AbstractBaseVO.class);
    }

    public void reset() {
        FieldUtils.getAllFieldsList(this.getClass())
                .stream()
                .forEach(field ->
                        ReflectionKits.invokeSetter(this, field.getName(), null)
                );
    }


}
