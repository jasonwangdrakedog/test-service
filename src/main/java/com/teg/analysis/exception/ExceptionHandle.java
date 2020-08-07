package com.teg.analysis.exception;

import com.teg.analysis.model.common.Result;
import com.teg.analysis.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author wangyuan
 * @date 2020/8/7 16:22
 */
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandle.class);

    /**
     * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionGet(Exception e) {
        if (e instanceof BusinessException) {
            BusinessException MyException = (BusinessException) e;
            LOGGER.error("【业务异常】{}", e);
            return ResultUtil.fail(MyException.getMessage());
        }

        LOGGER.error("【系统异常】{}", e);
        return ResultUtil.fail(ExceptionEnum.UNKNOW_ERROR);
    }
}