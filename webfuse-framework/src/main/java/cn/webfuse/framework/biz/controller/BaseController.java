package cn.webfuse.framework.biz.controller;

import cn.webfuse.framework.model.AbstractBaseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class BaseController<T extends AbstractBaseVO> {

    @Autowired
    protected HttpServletRequest request;

}
