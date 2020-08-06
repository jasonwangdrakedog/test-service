package com.teg.analysis.model.common;

import lombok.Data;

import java.util.List;

/**
 * @author wangyuan
 * @date 2020/8/6 17:27
 */
@Data
public class Page<T> {
    private List<T> data;

    private Boolean success;

    private String msg;

    private Integer page;

    private Integer pageSize;


    public static <T> Page success(List<T> data) {
        return new Page().data(data);
    }

    public static <T> Page fail(String msg) {
        return new Page().msg(msg);
    }


    public Page data(List<T> data) {
        this.data = data;
        this.success = true;
        return this;
    }

    public Page msg(String msg) {
        this.success = false;
        this.msg = msg;
        return this;
    }


}
