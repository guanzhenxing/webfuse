package cn.webfuse.framework.model;

import java.io.Serializable;
import java.util.Date;

/**
 * DO(Data Object):此对象与数据库表结构一一对应，通过 DAO 层向上传输数据源对象。
 */
public abstract class AbstractBaseDO<T> implements Serializable {

    protected T id;
    protected Date createTime;
    protected Date updateTime;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
