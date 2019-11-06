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

/**
 * CheckedException的wrapper.
 * <p>
 * 返回Message时, 将返回内层Exception的Message.
 * <p>
 * copy from vjtools
 */
public class UncheckedException extends RuntimeException {

    private static final long serialVersionUID = 4140223302171577501L;

    public UncheckedException(Throwable wrapped) {
        super(wrapped);
    }

    @Override
    public String getMessage() {
        return super.getCause().getMessage();
    }
}