package com.teg.analysis.model.common;

import lombok.Data;

/**
 * @author wangyuan
 * @date 2020/8/6 17:27
 */
@Data
public class Result<T> {
    private T data;

    private Boolean success;

    private String msg;


    public static <T> Result success(T data) {
        return new Result().data(data);
    }

    public static <T> Result fail(String msg) {
        return new Result().msg(msg);
    }


    public Result data(T data) {
        this.data = data;
        this.success = true;
        return this;
    }

    public Result msg(String msg) {
        this.success = false;
        this.msg = msg;
        return this;
    }


}
