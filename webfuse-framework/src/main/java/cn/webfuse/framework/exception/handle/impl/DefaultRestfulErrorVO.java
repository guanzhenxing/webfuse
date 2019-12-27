package cn.webfuse.framework.exception.handle.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * RestfulError的返回信息
 *
 * @author Jesen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultRestfulErrorVO {

    private String code;
    private String message;
    private Map<String, Object> detail;


}
