/*
 * Copyright [2018] [https://github.com/vipshop/vjtools]
 *
 * Further modifications copyright (c) 2019 by webfuse.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.webfuse.core.exception;


import cn.webfuse.core.kit.ExceptionKits;

/**
 * 适用于异常信息需要变更的情况, 可通过clone()，不经过构造函数（也就避免了获得StackTrace）地从之前定义的静态异常中克隆，再设定新的异常信息
 *
 * @see CloneableException
 * <p>
 * copy from vjtools
 */
public class CloneableRuntimeException extends RuntimeException implements Cloneable {

    private static final long serialVersionUID = 3984796576627959400L;

    protected String message;

    public CloneableRuntimeException() {
        super((Throwable) null);
    }

    public CloneableRuntimeException(String message) {
        super((Throwable) null);
        this.message = message;
    }

    public CloneableRuntimeException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public CloneableRuntimeException clone() {
        try {
            return (CloneableRuntimeException) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 简便函数，定义静态异常时使用
     *
     * @param throwClazz  抛出异常的类
     * @param throwMethod 抛出异常的方法
     * @return
     */
    public CloneableRuntimeException setStackTrace(Class<?> throwClazz, String throwMethod) {
        ExceptionKits.setStackTrace(this, throwClazz, throwMethod);
        return this;
    }

    /**
     * 简便函数, clone并重新设定Message
     *
     * @param message 异常的消息
     */
    public CloneableRuntimeException clone(String message) {
        CloneableRuntimeException newException = this.clone();
        newException.setMessage(message);
        return newException;
    }

    /**
     * 简便函数, 重新设定Message
     *
     * @param message 异常的消息
     */
    public CloneableRuntimeException setMessage(String message) {
        this.message = message;
        return this;
    }
}